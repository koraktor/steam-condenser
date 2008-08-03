package steamcondenser.steam;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.DatagramChannel;
import java.util.logging.Logger;

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
	
	abstract SteamPacket getReply()
		throws IOException, Exception;
	
	protected int receivePacket()
		throws IOException
	{
		return this.receivePacket(0);
	}
	
	protected int receivePacket(int bufferLength)
		throws IOException
	{
		int bytesRead;
		
		if(bufferLength == 0)
		{
			this.buffer.clear();
		}
		else
		{
			this.buffer = ByteBuffer.allocate(1400);
		}
		this.channel.receive(this.buffer);
		bytesRead = this.buffer.position();
		this.buffer.rewind();
		this.buffer.limit(bytesRead);
		
		return bytesRead;
	}
	
	/**
	 * @param dataPacket The {@link steamcondenser.steam.packets.SteamPacket SteamPacket} to send to the remote end
	 */
	public void send(SteamPacket dataPacket)
		throws IOException
	{
		Logger.getLogger("global").info("Sending data packet of type \"" + dataPacket.getClass().getSimpleName() + "\"");

		this.buffer = ByteBuffer.wrap(dataPacket.getBytes());
		this.channel.send(this.buffer, this.remoteSocket);
		this.buffer.flip();
	}
	
	public void finalize()
		throws IOException
	{
		this.channel.close();
	}
}
