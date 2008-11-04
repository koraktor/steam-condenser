/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Vector;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import steamcondenser.SteamCondenserException;
import steamcondenser.steam.packets.SteamPacket;

/**
 * A socket used for connections to Source game servers.
 * @author Sebastian Staudt
 * @version $Id$
 */
public class SourceSocket extends QuerySocket
{
    public SourceSocket(InetAddress ipAddress, int portNumber)
    throws IOException
    {
	super(ipAddress, portNumber);
    }

    public SteamPacket getReply()
    throws IOException, TimeoutException, SteamCondenserException
    {
	int bytesRead;
	SteamPacket packet;

	bytesRead = this.receivePacket(1400);

	if(this.packetIsSplit())
	{
	    boolean isCompressed = false;
	    byte[] splitData;
	    int packetCount, packetNumber, requestId;
	    int packetChecksum = 0;
	    short splitSize;
	    short uncompressedSize = 0;
	    Vector<byte[]> splitPackets = new Vector<byte[]>();

	    do
	    {
		// Parsing of split packet headers
		requestId = Integer.reverseBytes(this.buffer.getInt());

		isCompressed = this.packetIsCompressed(requestId);

		packetCount = this.buffer.get();
		packetNumber = this.buffer.get() + 1;
		splitSize = Short.reverseBytes(this.buffer.getShort());

		if(isCompressed)
		{
		    uncompressedSize = Short.reverseBytes(this.buffer.getShort());
		    packetChecksum = Integer.reverseBytes(this.buffer.getInt());
		}

		// Omit additional header on the first packet 
		if(packetNumber == 1)
		{
		    this.buffer.getInt();
		}

		// Caching of split packet Data
		splitData = new byte[splitSize];
		this.buffer.get(splitData);
		splitPackets.setSize(packetCount);
		splitPackets.set(packetNumber - 1, splitData);

		// Receiving the next packet
		bytesRead = this.receivePacket();

		Logger.getLogger("global").info("Received packet #" + packetNumber + " of " + packetCount + " for request ID " + requestId + ".");
	    }
	    while(bytesRead > 0 && Integer.reverseBytes(this.buffer.getInt()) == -2);

	    if(isCompressed)
	    {
		packet = SteamPacket.reassemblePacket(splitPackets, true, uncompressedSize, packetChecksum);
	    }
	    else
	    {
		packet = SteamPacket.reassemblePacket(splitPackets);
	    }
	}
	else
	{
	    packet = this.createPacket();
	}

	this.buffer.flip();

	Logger.getLogger("global").info("Received packet of type \"" + packet.getClass().getSimpleName() + "\"");

	return packet;
    }

    /**
     * Checks whether a packet is split or not by it's request ID
     * @param packetHeader The request ID of the packet to check
     * @return true if the packet is compressed, otherwise false
     */
    private boolean packetIsCompressed(int requestId)
    {
	return (requestId & 0x8000) != 0;
    }
}
