package steamcondenser.steam.packets;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @author Sebastian Staudt
 * @version $Id: A2A_PING_RequestPacket.java 26 2008-07-18 11:58:23Z koraktor $
 */
public class A2A_INFO_ResponsePacket extends SteamPacket
{
	private int networkVersion;
	
	public A2A_INFO_ResponsePacket(byte[] dataBytes)
	{
		super(SteamPacket.A2A_INFO_RESPONSE_HEADER, dataBytes);
		
		this.networkVersion = this.contentData.getByte();
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
