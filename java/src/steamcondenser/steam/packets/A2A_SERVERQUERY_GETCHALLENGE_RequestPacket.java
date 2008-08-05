/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.packets;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_SERVERQUERY_GETCHALLENGE_RequestPacket extends SteamPacket
{
	public A2A_SERVERQUERY_GETCHALLENGE_RequestPacket()
	{
		super(SteamPacket.A2A_SERVERQUERY_GETCHALLENGE_REQUEST_HEADER);
	}
}
