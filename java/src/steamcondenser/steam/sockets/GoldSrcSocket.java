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

import steamcondenser.NothingReceivedException;
import steamcondenser.SteamCondenserException;
import steamcondenser.UncompletePacketException;
import steamcondenser.steam.packets.SteamPacket;
import steamcondenser.steam.packets.SteamPacketFactory;
import steamcondenser.steam.packets.rcon.RCONGoldSrcRequestPacket;
import steamcondenser.steam.packets.rcon.RCONGoldSrcResponsePacket;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class GoldSrcSocket extends QuerySocket
{
    private int rconChallenge = 0;

    /**
     * 
     * @param ipAddress
     * @param portNumber
     * @throws IOException
     */
    public GoldSrcSocket(InetAddress ipAddress, int portNumber)
    throws IOException
    {
	super(ipAddress, portNumber);
    }

    /**
     * @return The SteamPacket received from the server
     * @throws IOException
     * @throws SteamCondenserException
     * @throws TimeoutException
     */
    public SteamPacket getReply()
    throws IOException, SteamCondenserException, TimeoutException
    {
	int bytesRead;
	SteamPacket packet;

	bytesRead = this.receivePacket(1400);

	if(this.packetIsSplit())
	{
	    byte[] splitData;
	    int packetCount, packetNumber;
	    int requestId;
	    byte packetNumberAndCount;
	    Vector<byte[]> splitPackets = new Vector<byte[]>();

	    do
	    {
		// Parsing of split packet headers
		requestId = Integer.reverseBytes(this.buffer.getInt());
		packetNumberAndCount = this.buffer.get();
		packetCount = packetNumberAndCount & 15;
		packetNumber = (packetNumberAndCount >> 4) + 1;

		// Omit additional header on the first packet 
		if(packetNumber == 1)
		{
		    this.buffer.getInt();
		}

		// Caching of split packet Data
		splitData = new byte[this.buffer.remaining()];
		this.buffer.get(splitData);
		splitPackets.setSize(packetCount);
		splitPackets.set(packetNumber - 1, splitData);

		// Receiving the next packet
		bytesRead = this.receivePacket();

		Logger.getLogger("global").info("Received packet #" + packetNumber + " of " + packetCount + " for request ID " + requestId + ".");
	    }
	    while(bytesRead > 0 && Integer.reverseBytes(this.buffer.getInt()) == -2);

	    packet = SteamPacketFactory.reassemblePacket(splitPackets);
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
     * Sends a RCON command with the specified password to a GoldSrc server
     * @param password RCON password to use
     * @param command RCON command to send to the server
     * @return The response send by the server
     * @throws IOException
     * @throws TimeoutException
     * @throws SteamCondenserException 
     * @throws UncompletePacketException 
     */
    public String rconExec(String password, String command)
    throws IOException, TimeoutException, SteamCondenserException
    {
	if(this.rconChallenge == 0)
	{
	    this.rconGetChallenge();
	}

	this.rconSend("rcon " + this.rconChallenge + " " + password + " "+ command);

	return ((RCONGoldSrcResponsePacket) this.getReply()).getResponse();
    }

    /**
     * @throws SteamCondenserException 
     * @throws NumberFormatException 
     * 
     */
    public void rconGetChallenge()
    throws IOException, TimeoutException, NumberFormatException, SteamCondenserException
    {
	this.rconSend("challenge rcon");
	int bytesRead = this.receivePacket(1400);

	if(bytesRead == 0)
	{
	    throw new NothingReceivedException();
	}

	this.rconChallenge = Integer.parseInt(new String(this.buffer.array()).substring(19, 29));
    }

    private void rconSend(String command)
    throws IOException
    {
	this.send(new RCONGoldSrcRequestPacket(command));
    }
}
