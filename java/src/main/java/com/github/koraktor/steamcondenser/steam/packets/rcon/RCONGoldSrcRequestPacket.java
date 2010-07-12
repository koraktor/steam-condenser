/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets.rcon;

import com.github.koraktor.steamcondenser.Helper;
import com.github.koraktor.steamcondenser.steam.packets.SteamPacket;

public class RCONGoldSrcRequestPacket extends SteamPacket
{
    public RCONGoldSrcRequestPacket(String request)
    {
	super((byte) 0, request.getBytes());
    }

    public byte[] getBytes()
    {
	byte[] bytes = new byte[this.contentData.getLength() + 4];

	System.arraycopy(Helper.byteArrayFromInteger(0xFFFFFFFF), 0, bytes, 0, 4);
	System.arraycopy(this.contentData.array(), 0, bytes, 4, this.contentData.getLength());

	return bytes;
    }
}
