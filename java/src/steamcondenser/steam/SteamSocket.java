package steamcondenser.steam;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import steamcondenser.PacketFormatException;
import steamcondenser.SteamCondenserException;
import steamcondenser.steam.packets.SteamPacket;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
abstract class SteamSocket
{
	protected ByteBuffer buffer;
	
	protected DatagramChannel channel;
	
	protected InetSocketAddress remoteSocket;
	
	/**
	 * @param ipAddress The IP of the server to connect to
	 * @param portNumber The port number of the server
	 */
	protected SteamSocket(InetAddress ipAddress, int portNumber)
		throws IOException
	{
		this.buffer = ByteBuffer.allocate(1400);
		this.buffer.order(ByteOrder.LITTLE_ENDIAN);
		
		this.channel = DatagramChannel.open();
		this.channel.configureBlocking(false);
		
		this.remoteSocket = new InetSocketAddress(ipAddress, portNumber);
	}
	
	/**
	 * Reads a single, non-split packet into a SteamPacket object
	 * @return The SteamPacket object created from the data in the buffer
	 * @throws PacketFormatException When the SteamPacket could not be created
	 *         because of an format error
	 */
	protected SteamPacket createPacket()
		throws PacketFormatException
	{
		byte[] packetData = new byte[this.buffer.remaining()];
		this.buffer.get(packetData);
		
		return SteamPacket.createPacket(packetData);
	}
	
	/**
	 * Subclasses have to implement this method for their individual packet
	 * format
	 * @return The SteamPacket received
	 * @throws IOException
	 * @throws Exception
	 */
	abstract SteamPacket getReply()
		throws IOException, TimeoutException, SteamCondenserException;
	
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
			this.buffer = ByteBuffer.allocate(1400);
			if(selector.select(1000) == 0)
			{
				throw new TimeoutException();
			}
		}
		
		this.channel.receive(this.buffer);
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
		this.channel.send(this.buffer, this.remoteSocket);
		this.buffer.flip();
	}
	
	/**
	 * Closes the DatagramChannel
	 */
	public void finalize()
		throws IOException
	{
		this.channel.close();
	}
}
