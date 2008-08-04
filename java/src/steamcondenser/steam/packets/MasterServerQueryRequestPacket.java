package steamcondenser.steam.packets;

public class MasterServerQueryRequestPacket extends SteamPacket
{
	public static final byte REGION_US_EAST_COAST = 0x00;
	public static final byte REGION_US_WEST_COAST = 0x01;
	public static final byte REGION_SOUTH_AMERICA = 0x02;
	public static final byte REGION_EUROPE = 0x03;
	public static final byte REGION_ASIA = 0x04;
	public static final byte REGION_AUSTRALIA = 0x05;
	public static final byte REGION_MIDDLE_EAST = 0x06;
	public static final byte REGION_AFRICA = 0x07;
	public static final byte REGION_ALL = 0x00;
	
	private String filter;
	private byte regionCode;
	private String startIp;
	
	public MasterServerQueryRequestPacket()
	{
		this(MasterServerQueryRequestPacket.REGION_ALL, "0.0.0.0:0", "");
	}
	
	public MasterServerQueryRequestPacket(byte regionCode, String startIp, String filter)
	{
		super(SteamPacket.MASTER_SERVER_QUERY_REQUEST_HEADER);
		
		this.filter = filter;
		this.regionCode = regionCode;
		this.startIp = startIp;
	}
	
	public byte[] getBytes()
	{
		byte[] bytes, filterBytes, startIpBytes;

		filterBytes = (this.filter + "\0").getBytes();
		startIpBytes = (this.startIp + "\0").getBytes();
		bytes = new byte[2 + startIpBytes.length + filterBytes.length];
		
		bytes[0] = this.headerData;
		bytes[1] = this.regionCode;
		System.arraycopy(startIpBytes, 0, bytes, 2, startIpBytes.length);
		System.arraycopy(filterBytes, 0, bytes, startIpBytes.length + 2, filterBytes.length);
		
		return bytes;
	}
}
