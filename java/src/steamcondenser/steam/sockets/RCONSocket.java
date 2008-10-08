/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeoutException;

import steamcondenser.PacketBuffer;
import steamcondenser.PacketFormatException;
import steamcondenser.SteamCondenserException;
import steamcondenser.steam.packets.rcon.RCONAuthResponse;
import steamcondenser.steam.packets.rcon.RCONExecResponsePacket;
import steamcondenser.steam.packets.rcon.RCONPacket;

public class RCONSocket extends SteamSocket
{
	public RCONSocket(InetAddress ipAddress, int portNumber)
		throws IOException
	{
		super(ipAddress, portNumber);
		
		this.buffer = ByteBuffer.allocate(1500);
		
		this.channel = SocketChannel.open();
	}
	
	public RCONPacket createPacket()
		throws PacketFormatException
	{
	    PacketBuffer packetBuffer = new PacketBuffer(this.buffer.array());
	    
		int packetSize = Integer.reverseBytes(packetBuffer.getInt());
		int requestId = Integer.reverseBytes(packetBuffer.getInt());
		int header = Integer.reverseBytes(packetBuffer.getInt());
		String data = packetBuffer.getString();
		
		switch(header)
		{
			case RCONPacket.SERVERDATA_AUTH_RESPONSE:
				return new RCONAuthResponse(requestId);
			case RCONPacket.SERVERDATA_RESPONSE_VALUE:
				return new RCONExecResponsePacket(requestId, data);
			default:
				throw new PacketFormatException("Unknown packet with header " + Integer.reverseBytes(header) + " received.");
		}
	}
	
	public void send(RCONPacket dataPacket)
		throws IOException
	{
		if(!((SocketChannel) this.channel).isConnected())
		{
			((SocketChannel) this.channel).connect(this.remoteSocket);
		}
		
		this.buffer = ByteBuffer.wrap(dataPacket.getBytes());
		((SocketChannel) this.channel).write(this.buffer);
	}

	public RCONPacket getReply()
		throws IOException, TimeoutException, SteamCondenserException
	{
	    this.buffer = ByteBuffer.allocate(1400);
		((SocketChannel) this.channel).read(this.buffer);
		
		return this.createPacket();
	}
}
