package steamcondenser.steam;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Vector;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import steamcondenser.steam.packets.SteamPacket;

public class SourceSocket extends SteamSocket
{
	public SourceSocket(InetAddress ipAddress, int portNumber)
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
	
		this.buffer = ByteBuffer.allocate(1400);
		this.channel.receive(this.buffer);
		bytesRead = this.buffer.position();
		this.buffer.rewind();
		this.buffer.limit(bytesRead);
		
		if(Integer.reverseBytes(this.buffer.getInt()) == -2)
		{
			byte[] splitData, tmpData;
			int packetCount, packetNumber;
			int requestId;
			short splitSize;
			Vector<byte[]> splitPackets = new Vector<byte[]>();
			packetData = new byte[0];
			
			do
			{
				// Parsing of split packet headers
				requestId = Integer.reverseBytes(this.buffer.getInt());
				packetCount = this.buffer.get();
				packetNumber = this.buffer.get() + 1;
				splitSize = Short.reverseBytes(this.buffer.getShort());
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
				this.buffer.clear();
				this.channel.receive(this.buffer);
				bytesRead = this.buffer.position();
				this.buffer.rewind();
				this.buffer.limit(bytesRead);
				
				Logger.getLogger("global").info("Received packet #" + packetNumber + " of " + packetCount + " for request ID " + requestId + ".");
			}
			while(bytesRead > 0 && Integer.reverseBytes(this.buffer.getInt()) == -2);
	
			for(byte[] splitPacket : splitPackets)
			{
				tmpData = packetData;
				packetData = new byte[tmpData.length + splitPacket.length];
				System.arraycopy(tmpData, 0, packetData, 0, tmpData.length);
				System.arraycopy(splitPacket, 0, packetData, tmpData.length, splitPacket.length);
			}
			
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
}
