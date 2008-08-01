package steamcondenser.steam.packets;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @author Sebastian Staudt
 * @version $Id: A2A_PING_RequestPacket.java 26 2008-07-18 11:58:23Z koraktor $
 */
public class A2A_INFO_ResponsePacket extends SteamPacket
{
	private short appId;
	private byte dedicated;
	private String gameDescription;
	private String gameDir;
	private String gameVersion;
	private String mapName;
	private int maxPlayers;
	private int networkVersion;
	private int numberOfBots;
	private int numberOfPlayers;
	private byte operatingSystem;
	private boolean passwordProtected;
	private boolean secure;
	private String serverName;
	private short serverPort;
	private String serverTags;
	private String tvName;
	private short tvPort;
	
	public A2A_INFO_ResponsePacket(byte[] dataBytes)
	{
		super(SteamPacket.A2A_INFO_RESPONSE_HEADER, dataBytes);
		
		this.networkVersion = this.contentData.getByte();
		this.serverName = this.contentData.getString();
		this.mapName = this.contentData.getString();
		this.gameDir = this.contentData.getString();
		this.gameDescription = this.contentData.getString();
		this.appId = Short.reverseBytes(this.contentData.getShort());
		this.numberOfPlayers = this.contentData.getByte();
		this.maxPlayers = this.contentData.getByte();
		this.numberOfBots = this.contentData.getByte();
		this.dedicated = this.contentData.getByte();
		this.operatingSystem = this.contentData.getByte();
		this.passwordProtected = this.contentData.getByte() == 1;
		this.secure = this.contentData.getByte() == 1;
		this.gameVersion = this.contentData.getString();
		byte extraDataFlag = this.contentData.getByte();
		
		if((extraDataFlag & 0x80) == 1)
		{
			this.serverPort = Short.reverseBytes(this.contentData.getShort());
		}
		
		if((extraDataFlag & 0x40) == 1)
		{
			this.tvPort = Short.reverseBytes(this.contentData.getShort());
			this.tvName = this.contentData.getString();
		}
		
		if((extraDataFlag & 0x20) == 1)
		{
			this.serverTags = this.contentData.getString();
		}
	}
	
	public HashMap<String, Object> getInfoHash()
	{
		HashMap<String, Object> infoHash = new HashMap<String, Object>();
		
		try
		{
			for(Field field : this.getClass().getDeclaredFields())
			{
				infoHash.put(field.getName(), field.get(this));
			}
		}
		catch(IllegalAccessException e){}
		
		return infoHash;
	}
}
