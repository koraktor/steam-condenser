/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package com.github.koraktor.steamcondenser.steam.packets;

/**
 * @author Sebastian Staudt
 */
public class A2A_PING_Packet extends SteamPacket
{
    public A2A_PING_Packet()
    {
	super(SteamPacket.A2A_PING_HEADER);
    }
}
