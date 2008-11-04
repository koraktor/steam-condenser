/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.packets;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2S_INFO_Packet extends SteamPacket
{
    public A2S_INFO_Packet()
    {
	super(SteamPacket.A2S_INFO_HEADER, "Source Engine Query".getBytes());
    }
}
