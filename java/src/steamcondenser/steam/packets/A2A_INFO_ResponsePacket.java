package steamcondenser.steam.packets;

/**
 * @author Sebastian Staudt
 * @version $Id: A2A_PING_RequestPacket.java 26 2008-07-18 11:58:23Z koraktor $
 */
public class A2A_INFO_ResponsePacket extends SteamPacket
{
	public A2A_INFO_ResponsePacket(byte[] dataBytes)
	{
		super(SteamPacket.A2A_INFO_RESPONSE_HEADER, dataBytes);
	}
}
