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
public class S2C_CHALLENGE_Packet extends SteamPacket
{
    /**
     * Creates a A2A_SERVERQUERY_GETCHALLENGE response base on a challenge
     * number
     * @param challengeNumberBytes A byte[] representation of the challenge
     *        number to send in this packet
     */
    public S2C_CHALLENGE_Packet(byte[] challengeNumberBytes)
    {
	super(SteamPacket.S2C_CHALLENGE_HEADER, challengeNumberBytes);
    }

    /**
     * @return The challenge number contained in this packet
     */
    public int getChallengeNumber()
    {
	return Integer.reverseBytes(this.contentData.getInt());
    }
}
