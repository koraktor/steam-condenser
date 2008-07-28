package steamcondenser.steam.packets;

import java.nio.ByteBuffer;

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
	
	private byte[] contentData;
	private byte headerData;
	
	public SteamPacket(byte headerData)
	{
		this(headerData, new byte[0]);
	}
	
	public SteamPacket(byte headerData, byte[] contentData)
	{
		this.contentData = contentData;
		this.headerData = headerData;
	}
	
	public byte[] getBytes()
	{
		ByteBuffer packetData = ByteBuffer.allocate(this.contentData.length + 5);
		if(this.contentData.length < 1395)
		{
			packetData.putInt(-1);
		}
		else
		{
			packetData.putInt(-2);
		}
		packetData.put(this.headerData);
		packetData.put(this.contentData);
		return packetData.array();
	}
	
	public byte[] getData()
	{
		return this.contentData;
	}
	
	public byte getHeader()
	{
		return this.headerData;
	}
}
