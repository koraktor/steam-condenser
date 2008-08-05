/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.packets;

/**
 * A A2A_SERVERQUERY_GETCHALLENGE response
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket extends SteamPacket
{
	/**
	 * Creates a A2A_SERVERQUERY_GETCHALLENGE response base on a challenge
	 * number
	 * @param challengeNumberBytes A byte[] representation of the challenge
	 *        number to send in this packet
	 */
	public A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket(byte[] challengeNumberBytes)
	{
		super(SteamPacket.A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER, challengeNumberBytes);
	}
	
	/**
	 * @return The challenge number contained in this packet
	 */
	public int getChallengeNumber()
	{
		return Integer.reverseBytes(this.contentData.getInt());
	}
}
