package steamcondenser.steam;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Vector;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import steamcondenser.SteamCondenserException;
import steamcondenser.steam.packets.SteamPacket;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class SourceSocket extends SteamSocket
{
	public SourceSocket(InetAddress ipAddress, int portNumber)
		throws IOException
	{
		super(ipAddress, portNumber);
	}
	
	public SteamPacket getReply()
		throws IOException, TimeoutException, SteamCondenserException
	{
		int bytesRead;
		SteamPacket packet;
	
		bytesRead = this.receivePacket(1400);
		
		if(this.packetIsSplit())
		{
			byte[] splitData;
			int packetCount, packetNumber;
			int requestId;
			short splitSize;
			Vector<byte[]> splitPackets = new Vector<byte[]>();
			
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
				bytesRead = this.receivePacket();
				
				Logger.getLogger("global").info("Received packet #" + packetNumber + " of " + packetCount + " for request ID " + requestId + ".");
			}
			while(bytesRead > 0 && Integer.reverseBytes(this.buffer.getInt()) == -2);
	
			packet = SteamPacket.reassemblePacket(splitPackets);
		}
		else
		{
			packet = this.createPacket();
		}
	
		this.buffer.flip();
		
		Logger.getLogger("global").info("Received packet of type \"" + packet.getClass().getSimpleName() + "\"");
		
		return packet;
	}
}
