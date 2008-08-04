package steamcondenser.steam.packets;

import java.util.Vector;

import steamcondenser.PacketFormatException;

public class MasterServerQueryResponsePacket extends SteamPacket
{
	private Vector<String> serverArray;
	
	public MasterServerQueryResponsePacket(byte[] data)
		throws PacketFormatException
	{
		super(SteamPacket.MASTER_SERVER_QUERY_REQUEST_HEADER, data);
		
		if(this.contentData.getByte() != 10)
		{
			throw new PacketFormatException("Master query response is missing additional 0x0A byte.");
		}
		
		int firstOctet, secondOctet, thirdOctet, fourthOctet, portNumber;
		this.serverArray = new Vector<String>();
		
		do
		{
			firstOctet = this.contentData.getByte() & 0xFF;
			secondOctet = this.contentData.getByte() & 0xFF;
			thirdOctet = this.contentData.getByte() & 0xFF;
			fourthOctet = this.contentData.getByte() & 0xFF;
			portNumber = this.contentData.getShort() & 0xFFFF;
			
			this.serverArray.add(firstOctet + "." + secondOctet + "." + thirdOctet + "." + fourthOctet + ":" + portNumber);
		}
		while(this.contentData.remaining() > 0);
	}
	
	public Vector<String> getServers()
	{
		return this.serverArray;
	}
}
