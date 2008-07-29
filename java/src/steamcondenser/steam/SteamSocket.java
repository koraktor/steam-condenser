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
	private ByteBuffer readBuffer;
	
	private ByteBuffer writeBuffer;
	
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
	}
	
	public SteamPacket getReply()
	  throws IOException, Exception
	{
		SteamPacket replyPacket = this.readPacket();

		Logger.getLogger("global").info("Sending data packet of type \"" + replyPacket.getClass().getSimpleName() + "\"");

		return replyPacket;
	}
	
	private SteamPacket readPacket()
		throws IOException, Exception
	{
		byte[] packetData = new byte[0];
		
		this.readBuffer = ByteBuffer.allocate(1400);
		this.channel.read(this.readBuffer);
		
		if(this.readBuffer.getLong() == -2)
		{
			byte[] splitData, tmpData;
			int packetCount, packetNumber;
			long requestId;
			short splitSize;
			
			do
			{
				requestId = this.readBuffer.getLong();
				packetCount = this.readBuffer.get();
				packetNumber = this.readBuffer.get() + 1;
				splitSize = this.readBuffer.getShort();
				// Omit additional header on the first packet 
				if(packetNumber == 1)
				{
					this.readBuffer.getLong();
				}
				
				splitData = new byte[this.readBuffer.remaining()];
				this.readBuffer.get(splitData);
				tmpData = packetData;
				packetData = new byte[tmpData.length + splitData.length];				
				System.arraycopy(splitData, 0, packetData, packetData.length, splitData.length);
				
				this.channel.read(this.readBuffer);
			}
			while(packetNumber < packetCount && this.readBuffer.getLong() == -2);
			
			return SteamPacket.createPacket(packetData);
		}
		else
		{
			packetData = new byte[this.readBuffer.remaining()];
			this.readBuffer.get(packetData);
			return SteamPacket.createPacket(packetData);
		}
	}
	
	/**
	 * @param dataPacket The {@link steamcondenser.steam.packets.SteamPacket SteamPacket} to send to the remote end
	 */
	public void send(SteamPacket dataPacket)
		throws IOException
	{
		Logger.getLogger("global").info("Sending data packet of type \"" + dataPacket.getClass().getSimpleName() + "\"");

		byte[] data = dataPacket.getBytes();
		this.writeBuffer = ByteBuffer.wrap(data);
		this.channel.write(this.writeBuffer);
	}
	
	public void finalize()
		throws IOException
	{
		this.channel.close();
	}
}
