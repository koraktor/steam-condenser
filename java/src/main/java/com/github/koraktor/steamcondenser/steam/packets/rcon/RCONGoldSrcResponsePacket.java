/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets.rcon;

import com.github.koraktor.steamcondenser.steam.packets.SteamPacket;

public class RCONGoldSrcResponsePacket extends SteamPacket
{
    public RCONGoldSrcResponsePacket(byte[] commandResponse)
    {
	super(SteamPacket.RCON_GOLDSRC_RESPONSE_HEADER, commandResponse);
    }

    public String getResponse()
    {
	return this.contentData.getString();
    }
}
