/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.packets;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_INFO_RequestPacket extends SteamPacket
{
	public A2A_INFO_RequestPacket()
	{
		super(SteamPacket.A2A_INFO_REQUEST_HEADER, "Source Engine Query".getBytes());
	}
}
