package steamcondenser.steam.packets;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket extends SteamPacket
{
	public A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket(byte[] challengeNumberBytes)
	{
		super(SteamPacket.A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER, challengeNumberBytes);
	}
	
	public int getChallengeNumber()
	{
		return Integer.reverseBytes(this.contentData.getInt());
	}
}
