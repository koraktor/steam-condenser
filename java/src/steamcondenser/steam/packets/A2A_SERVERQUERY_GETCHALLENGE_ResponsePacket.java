package steamcondenser.steam.packets;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket extends SteamPacket
{
	public A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket(int challengeNumber)
	{
		super(SteamPacket.A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER, String.valueOf(challengeNumber).getBytes());
	}
	
	public int getChallengeNumber()
	{
		return this.contentData.getInt();
	}
}
