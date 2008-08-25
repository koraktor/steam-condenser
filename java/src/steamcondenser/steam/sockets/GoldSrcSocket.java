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
 * @author Sebastian Staudt
 * @version $Id$
 */
public class GoldSrcSocket extends QuerySocket
{
	public GoldSrcSocket(InetAddress ipAddress, int portNumber)
		throws IOException
	{
		super(ipAddress, portNumber);
	}
	
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
	
			packet = SteamPacket.reassemblePacket(splitPackets);
		}
		else
		{
			packet = this.createPacket();
		}
	
		this.buffer.flip();
		
		Logger.getLogger("global").info("Received packet of type \"" + packet.getClass().getSimpleName() + "\"");
		
		return packet;
	}
}
