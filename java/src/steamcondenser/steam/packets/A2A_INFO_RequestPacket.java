package steamcondenser.steam.packets;

/**
 * @author Sebastian Staudt
 * @version $Id: A2A_PING_RequestPacket.java 26 2008-07-18 11:58:23Z koraktor $
 */
public class A2A_INFO_RequestPacket extends SteamPacket
{
	public A2A_INFO_RequestPacket()
	{
		super(SteamPacket.A2A_INFO_REQUEST_HEADER);
	}
}
