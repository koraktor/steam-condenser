/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets.rcon;

import com.github.koraktor.steamcondenser.Helper;
import com.github.koraktor.steamcondenser.steam.packets.SteamPacket;

abstract public class RCONPacket extends SteamPacket
{
    public static final byte SERVERDATA_AUTH = 3;
    public static final byte SERVERDATA_AUTH_RESPONSE = 2;
    public static final byte SERVERDATA_EXECCOMMAND = 2;
    public static final byte SERVERDATA_RESPONSE_VALUE = 0;

    protected int header;

    protected int requestId;

    protected RCONPacket(int requestId, int rconHeader, String rconData)
    {
	super((byte) 0, (rconData + "\0\0").getBytes());

	this.header = rconHeader;
	this.requestId = requestId;
    }

    public byte[] getBytes()
    {
	byte[] bytes = new byte[this.contentData.getLength() + 12];

	System.arraycopy(Helper.byteArrayFromInteger(Integer.reverseBytes(bytes.length - 4)), 0, bytes, 0, 4);
	System.arraycopy(Helper.byteArrayFromInteger(Integer.reverseBytes(this.requestId)), 0, bytes, 4, 4);
	System.arraycopy(Helper.byteArrayFromInteger(Integer.reverseBytes(this.header)), 0, bytes, 8, 4);
	System.arraycopy(this.contentData.array(), 0, bytes, 12, bytes.length - 12);

	return bytes;
    }

    public int getRequestId()
    {
	return this.requestId;
    }

}
