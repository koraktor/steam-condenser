/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import steamcondenser.steam.packets.SteamPacket;

public abstract class QuerySocket extends SteamSocket
{
    protected QuerySocket(InetAddress ipAddress, int portNumber)
    throws IOException
    {
	super(ipAddress, portNumber);
	this.channel = DatagramChannel.open();
	this.channel.configureBlocking(false);
    }

    /**
     * Checks whether a packet in the buffer is split or not
     * @return true if the packet is split, otherwise false
     */
    protected boolean packetIsSplit()
    {
	return (Integer.reverseBytes(this.buffer.getInt()) == -2);
    }

    /**
     * Reads an UDP packet into an existing buffer
     * @return The number of bytes received
     * @throws IOException
     * @throws TimeoutException
     */
    protected int receivePacket()
    throws IOException, TimeoutException
    {
	return this.receivePacket(0);
    }

    /**
     * Reads an UDP packet into an existing or a new buffer
     * @param bufferLength The length of the new buffer to created or 0 to use
     *        the existing buffer
     * @return The number of bytes received
     * @throws IOException
     * @throws TimeoutException
     */
    protected int receivePacket(int bufferLength)
    throws IOException, TimeoutException
    {
	Selector selector = Selector.open();
	this.channel.register(selector, SelectionKey.OP_READ);

	int bytesRead;

	if(bufferLength == 0)
	{
	    this.buffer.clear();
	    selector.selectNow();
	}
	else
	{
	    this.buffer = ByteBuffer.allocate(bufferLength);
	    if(selector.select(1000) == 0)
	    {
		throw new TimeoutException();
	    }
	}

	((DatagramChannel) this.channel).receive(this.buffer);
	bytesRead = this.buffer.position();
	this.buffer.rewind();
	this.buffer.limit(bytesRead);

	return bytesRead;
    }

    /**
     * Sends a SteamPacket object over the UDP channel to the remote end
     * @param dataPacket The {@link steamcondenser.steam.packets.SteamPacket
     *        SteamPacket} to send to the remote end
     */
    public void send(SteamPacket dataPacket)
    throws IOException
    {
	Logger.getLogger("global").info("Sending data packet of type \"" + dataPacket.getClass().getSimpleName() + "\"");

	this.buffer = ByteBuffer.wrap(dataPacket.getBytes());
	((DatagramChannel) this.channel).send(this.buffer, this.remoteSocket);
	this.buffer.flip();
    }
}
