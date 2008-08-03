package steamcondenser.steam;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Vector;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import steamcondenser.steam.packets.SteamPacket;

public class GoldSrcSocket extends SteamSocket
{
	public GoldSrcSocket(InetAddress ipAddress, int portNumber)
		throws IOException
	{
		super(ipAddress, portNumber);
	}
	
	public SteamPacket getReply()
		throws IOException, Exception
	{
		Selector selector = Selector.open();
		this.channel.register(selector, SelectionKey.OP_READ);
		if(selector.select(1000) == 0)
		{
			throw new TimeoutException();
		}
		
		int bytesRead;
		byte[] packetData;
		SteamPacket packet;
	
		bytesRead = this.receivePacket(1400);
		
		if(Integer.reverseBytes(this.buffer.getInt()) == -2)
		{
			byte[] splitData;
			int packetCount, packetNumber;
			int requestId;
			byte packetNumberAndCount;
			Vector<byte[]> splitPackets = new Vector<byte[]>();
			packetData = new byte[0];
			
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
			packetData = new byte[this.buffer.remaining()];
			this.buffer.get(packetData);
			packet = SteamPacket.createPacket(packetData);
		}
	
		this.buffer.flip();
		
		Logger.getLogger("global").info("Received packet of type \"" + packet.getClass().getSimpleName() + "\"");
		
		return packet;
	}
}
