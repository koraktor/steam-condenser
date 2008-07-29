package steamcondenser.steam.packets;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_PLAYER_RequestPacket extends RequestPacketWithChallenge
{
	public A2A_PLAYER_RequestPacket(long challengeNumber)
	{
		super(SteamPacket.A2A_PLAYER_REQUEST_HEADER, challengeNumber);
	}
}
