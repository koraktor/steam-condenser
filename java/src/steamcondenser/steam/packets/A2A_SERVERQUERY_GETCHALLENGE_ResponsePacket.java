package steamcondenser.steam.packets;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket extends SteamPacket
{
	public A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket(long challengeNumber)
	{
		super(SteamPacket.A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER, Long.toString(challengeNumber).getBytes());
	}
	
	public long getChallengeNumber()
	{
		return Long.valueOf(this.contentData.toString());
	}
}
