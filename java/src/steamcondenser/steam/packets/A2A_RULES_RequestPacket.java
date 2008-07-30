package steamcondenser.steam.packets;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_RULES_RequestPacket extends SteamPacket
{
	public A2A_RULES_RequestPacket(int challengeNumber)
	{
		super(SteamPacket.A2A_RULES_REQUEST_HEADER, String.valueOf(challengeNumber).getBytes());
	}
}
