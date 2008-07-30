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

import steamcondenser.steam.packets.SteamPacket;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class SteamSocket
{
	private ByteBuffer buffer;
	
	private DatagramChannel channel;
	
	private Selector selector;
	
	/**
	 * @param ipAddress The IP of the server to connect to
	 * @param portNumber The port number of the server
	 */
	public SteamSocket(InetAddress ipAddress, int portNumber)
		throws IOException
	{
		this.buffer = ByteBuffer.allocate(1400);
		this.buffer.order(ByteOrder.LITTLE_ENDIAN);
		
		this.selector = Selector.open();
		
		this.channel = DatagramChannel.open();
		this.channel.connect(new InetSocketAddress(ipAddress, portNumber));
		this.channel.configureBlocking(false);
		
		this.channel.register(this.selector, SelectionKey.OP_READ);
	}
	
	public SteamPacket getReply()
		throws IOException, Exception
	{
		if(this.selector.select(1000) == 0)
		{
			throw new TimeoutException();
		}
		
		int bytesRead;
		byte[] packetData = new byte[0];
		
		SteamPacket packet;

		this.buffer = ByteBuffer.allocate(1400);
		bytesRead = this.channel.read(this.buffer);
		this.buffer.rewind();
		this.buffer.limit(bytesRead);
		
		if(this.buffer.getInt() == -2L)
		{
			byte[] splitData, tmpData;
			int packetCount, packetNumber;
			long requestId;
			short splitSize;
			
			do
			{
				requestId = this.buffer.getInt();
				packetCount = this.buffer.get();
				packetNumber = this.buffer.get() + 1;
				splitSize = this.buffer.getShort();
				// Omit additional header on the first packet 
				if(packetNumber == 1)
				{
					this.buffer.getInt();
				}
				
				splitData = new byte[this.buffer.remaining()];
				this.buffer.get(splitData);
				tmpData = packetData;
				packetData = new byte[tmpData.length + splitData.length];
				System.arraycopy(splitData, 0, packetData, packetData.length, splitData.length);
				
				this.buffer.clear();
				this.channel.read(this.buffer);
				this.buffer.rewind();
				this.buffer.limit(bytesRead);
			}
			while(packetNumber < packetCount && this.buffer.getInt() == -2L);
			
			packet = SteamPacket.createPacket(packetData);
		}
		else
		{
			packetData = new byte[this.buffer.remaining()];
			this.buffer.get(packetData);
			packet = SteamPacket.createPacket(packetData);
		}
		
		this.buffer.flip();
		
		Logger.getLogger("global").info("Received packet of type \"" + packet.getClass().getSimpleName() + "\"");
		
		return packet;
	}
	
	/**
	 * @param dataPacket The {@link steamcondenser.steam.packets.SteamPacket SteamPacket} to send to the remote end
	 */
	public void send(SteamPacket dataPacket)
		throws IOException
	{
		Logger.getLogger("global").info("Sending data packet of type \"" + dataPacket.getClass().getSimpleName() + "\"");

		this.buffer = ByteBuffer.wrap(dataPacket.getBytes());
		this.channel.write(this.buffer);
		this.buffer.flip();
	}
	
	public void finalize()
		throws IOException
	{
		this.channel.close();
	}
}
