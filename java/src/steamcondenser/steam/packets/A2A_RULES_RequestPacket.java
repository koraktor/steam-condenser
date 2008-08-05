/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.packets;

import steamcondenser.Helper;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_RULES_RequestPacket extends SteamPacket
{
	public A2A_RULES_RequestPacket(int challengeNumber)
	{
		super(SteamPacket.A2A_RULES_REQUEST_HEADER, Helper.byteArrayFromInteger(Integer.reverseBytes(challengeNumber)));
	}
}
