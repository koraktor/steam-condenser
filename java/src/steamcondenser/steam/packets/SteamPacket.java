package steamcondenser.steam.packets;

import steamcondenser.PacketBuffer;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class SteamPacket
{
	public static final byte A2A_INFO_REQUEST_HEADER = 0x54;
	public static final byte A2A_INFO_RESPONSE_HEADER = 0x49;
	public static final byte A2A_PING_REQUEST_HEADER = 0x69;
	public static final byte A2A_PING_RESPONSE_HEADER = 0x6A;
	public static final byte A2A_PLAYER_REQUEST_HEADER = 0x55;
	public static final byte A2A_PLAYER_RESPONSE_HEADER = 0x44;
	public static final byte A2A_RULES_REQUEST_HEADER = 0x56;
	public static final byte A2A_RULES_RESPONSE_HEADER = 0x45;
	public static final byte A2A_SERVERQUERY_GETCHALLENGE_REQUEST_HEADER = 0x57;
	public static final byte A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER = 0x41;
	
	protected PacketBuffer contentData;
	private byte headerData;
	
	public static SteamPacket createPacket(byte[] rawData)
	  throws Exception
	{
		byte header = rawData[0];
		byte[] data = new byte[rawData.length - 1];
		System.arraycopy(rawData, 1, data, 0, rawData.length - 1);
		
		switch(header)
		{
			case SteamPacket.A2A_INFO_REQUEST_HEADER:
				return new A2A_INFO_RequestPacket();
				
			case SteamPacket.A2A_INFO_RESPONSE_HEADER:
				return new A2A_INFO_ResponsePacket(data);
			
			case SteamPacket.A2A_PING_REQUEST_HEADER:
				return new A2A_PING_RequestPacket();
				
			case SteamPacket.A2A_PING_RESPONSE_HEADER:
				return new A2A_PING_ResponsePacket(data);
				
			case SteamPacket.A2A_PLAYER_REQUEST_HEADER:
				return new A2A_PLAYER_RequestPacket(Integer.valueOf(new String(data)));
			
			case SteamPacket.A2A_PLAYER_RESPONSE_HEADER:
				return new A2A_PLAYER_ResponsePacket(data);
				
			case SteamPacket.A2A_RULES_REQUEST_HEADER:
				return new A2A_RULES_RequestPacket(Integer.valueOf(new String(data)));
			
			case SteamPacket.A2A_RULES_RESPONSE_HEADER:
				return new A2A_RULES_ResponsePacket(data);
				
			case SteamPacket.A2A_SERVERQUERY_GETCHALLENGE_REQUEST_HEADER:
				return new A2A_SERVERQUERY_GETCHALLENGE_RequestPacket();
				
			case SteamPacket.A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER:
				return new A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket(Integer.valueOf(new String(data)));
				
			default:
				throw new Exception("Unknown packet with header 0x" + header + " received.");
		}
	}
	
	public SteamPacket(byte headerData)
	{
		this(headerData, new byte[0]);
	}
	
	public SteamPacket(byte headerData, byte[] contentBytes)
	{
		this.contentData = new PacketBuffer(contentBytes);
		this.headerData = headerData;
	}
	
	public byte getHeader()
	{
		return this.headerData;
	}
}
