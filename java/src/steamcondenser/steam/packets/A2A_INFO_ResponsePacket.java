package steamcondenser.steam.packets;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @author Sebastian Staudt
 * @version $Id: A2A_PING_RequestPacket.java 26 2008-07-18 11:58:23Z koraktor $
 */
public abstract class A2A_INFO_ResponsePacket extends SteamPacket
{
	protected byte dedicated;
	protected String gameDescription;
	protected String gameDir;
	protected String mapName;
	protected int maxPlayers;
	protected int networkVersion;
	protected int numberOfBots;
	protected int numberOfPlayers;
	protected byte operatingSystem;
	protected boolean passwordProtected;
	protected boolean secure;
	protected String serverName;
	
	A2A_INFO_ResponsePacket(byte headerByte, byte[] dataBytes)
	{
		super(headerByte, dataBytes);
	}
	
	public HashMap<String, Object> getInfoHash()
		throws IllegalAccessException
	{
		HashMap<String, Object> infoHash = new HashMap<String, Object>();

			for(Field field : this.getClass().getSuperclass().getDeclaredFields())
			{
				infoHash.put(field.getName(), field.get(this));
			}
			
			Field[] fields = this.getClass().getDeclaredFields();
			for(int i = 0; i < fields.length; i++)
			{
				infoHash.put(fields[i].getName(), fields[i].get(this));
			}
		
		return infoHash;
	}
}
