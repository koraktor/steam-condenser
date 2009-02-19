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
import steamcondenser.steam.packets.SteamPacketFactory;

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
	    Vector<byte[]> splitPackets = new Vector<byte[]>();

	    do
	    {
		// Parsing of split packet headers
		requestId = Integer.reverseBytes(this.buffer.getInt());
		isCompressed = ((requestId & 0x8000) != 0);
		packetCount = this.buffer.get();
		packetNumber = this.buffer.get() + 1;

		if(isCompressed)
		{
		    splitSize = Short.reverseBytes(this.buffer.getShort());
		    packetChecksum = Integer.reverseBytes(this.buffer.getInt());
		}
		else {
		    splitSize = Short.reverseBytes(this.buffer.getShort());
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
		packet = SteamPacketFactory.reassemblePacket(splitPackets, true, splitSize, packetChecksum);
	    }
	    else
	    {
		packet = SteamPacketFactory.reassemblePacket(splitPackets);
	    }
	}
	else
	{
	    packet = this.getPacketFromData();
	}

	this.buffer.flip();

	Logger.getLogger("global").info("Received packet of type \"" + packet.getClass().getSimpleName() + "\"");

	return packet;
    }
}
