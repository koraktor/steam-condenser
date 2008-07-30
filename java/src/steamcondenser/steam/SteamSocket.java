package steamcondenser.steam;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
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
	
	/**
	 * @param ipAddress The IP of the server to connect to
	 * @param portNumber The port number of the server
	 */
	public SteamSocket(InetAddress ipAddress, int portNumber)
		throws IOException
	{
		this.channel = DatagramChannel.open();
		this.channel.connect(new InetSocketAddress(ipAddress, portNumber));
		this.buffer = ByteBuffer.allocate(1400);
	}
	
	public SteamPacket getReply()
		throws IOException, Exception
	{
		byte[] packetData = new byte[0];
		SteamPacket packet;

		this.buffer.clear();
		this.channel.read(this.buffer);
		String s = new String(this.buffer.array());
		System.out.println("received:" + s);
		
		if(this.buffer.getLong() == -2L)
		{
			byte[] splitData, tmpData;
			int packetCount, packetNumber;
			long requestId;
			short splitSize;
			
			do
			{
				requestId = this.buffer.getLong();
				packetCount = this.buffer.get();
				packetNumber = this.buffer.get() + 1;
				splitSize = this.buffer.getShort();
				// Omit additional header on the first packet 
				if(packetNumber == 1)
				{
					this.buffer.getLong();
				}
				
				splitData = new byte[this.buffer.remaining()];
				this.buffer.get(splitData);
				tmpData = packetData;
				packetData = new byte[tmpData.length + splitData.length];				
				System.arraycopy(splitData, 0, packetData, packetData.length, splitData.length);
				
				this.buffer.clear();
				this.channel.read(this.buffer);
			}
			while(packetNumber < packetCount && this.buffer.getLong() == -2L);
			
			packet = SteamPacket.createPacket(packetData);
		}
		else
		{
			packetData = new byte[this.buffer.remaining()];
			this.buffer.get(packetData);
			packet = SteamPacket.createPacket(packetData);
		}
		
		this.buffer.flip();
		
		Logger.getLogger("global").info("Sending data packet of type \"" + packet.getClass().getSimpleName() + "\"");
		
		return packet;
	}
	
	/**
	 * @param dataPacket The {@link steamcondenser.steam.packets.SteamPacket SteamPacket} to send to the remote end
	 */
	public void send(SteamPacket dataPacket)
		throws IOException
	{
		Logger.getLogger("global").info("Sending data packet of type \"" + dataPacket.getClass().getSimpleName() + "\"");

		byte[] data = dataPacket.getBytes();
		this.buffer = ByteBuffer.wrap(data);
		this.channel.write(this.buffer);
		this.buffer.flip();
	}
	
	public void finalize()
		throws IOException
	{
		this.channel.close();
	}
}
