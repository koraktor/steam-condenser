/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets.rcon;

import com.github.koraktor.steamcondenser.Helper;
import com.github.koraktor.steamcondenser.steam.packets.SteamPacket;

/**
 * This packet class represents a RCON request packet sent to a GoldSrc server
 * <p>
 * It is used to request a command execution on the server.
 *
 * @author Sebastian Staudt
 * @see com.github.koraktor.steamcondenser.steam.servers.GoldSrcServer#rconExec
 */
public class RCONGoldSrcRequestPacket extends SteamPacket
{
    /**
     * Creates a request for the given request string
     * <p>
     * The request string has the form <code>rcon {challenge number} {RCON
     * password} {command}</code>.
     *
     * @param request The request string to send to the server
     */
    public RCONGoldSrcRequestPacket(String request)
    {
	super((byte) 0, request.getBytes());
    }

    /**
     * Returns the raw data representing this packet
     *
     * @return A byte array containing the raw data of this request packet
     */
    public byte[] getBytes()
    {
	byte[] bytes = new byte[this.contentData.getLength() + 4];

	System.arraycopy(Helper.byteArrayFromInteger(0xFFFFFFFF), 0, bytes, 0, 4);
	System.arraycopy(this.contentData.array(), 0, bytes, 4, this.contentData.getLength());

	return bytes;
    }
}
