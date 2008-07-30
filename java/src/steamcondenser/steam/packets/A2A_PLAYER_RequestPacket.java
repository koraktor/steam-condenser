package steamcondenser.steam.packets;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_PLAYER_RequestPacket extends SteamPacket
{
	public A2A_PLAYER_RequestPacket(int challengeNumber)
	{
		super(SteamPacket.A2A_PLAYER_REQUEST_HEADER, String.valueOf(challengeNumber).getBytes());
	}
}
