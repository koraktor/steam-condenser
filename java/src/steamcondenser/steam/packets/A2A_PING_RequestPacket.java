package steamcondenser.steam.packets;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_PING_RequestPacket extends SteamPacket
{
	public A2A_PING_RequestPacket()
	{
		super(SteamPacket.A2A_PING_REQUEST_HEADER);
	}
}
