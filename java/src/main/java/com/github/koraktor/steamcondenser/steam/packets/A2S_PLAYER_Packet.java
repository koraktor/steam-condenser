/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

import com.github.koraktor.steamcondenser.Helper;

/**
 * @author Sebastian Staudt
 */
public class A2S_PLAYER_Packet extends SteamPacket
{
    public A2S_PLAYER_Packet(int challengeNumber)
    {
	super(SteamPacket.A2S_PLAYER_HEADER, Helper.byteArrayFromInteger(Integer.reverseBytes(challengeNumber)));
    }
}
