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

import steamcondenser.SteamCondenserException;
import steamcondenser.steam.packets.rcon.RCONPacket;
import steamcondenser.steam.packets.rcon.RCONPacketFactory;

public class RCONSocket extends SteamSocket
{
    public RCONSocket(InetAddress ipAddress, int portNumber)
    throws IOException
    {
	super(ipAddress, portNumber);

	this.buffer = ByteBuffer.allocate(1500);

	this.channel = SocketChannel.open();
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

	return RCONPacketFactory.getPacketFromData(this.buffer.array());
    }
}
