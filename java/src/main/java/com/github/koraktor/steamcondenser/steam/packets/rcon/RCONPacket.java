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
 * This module is included by all classes representing a packet used by
 * Source's RCON protocol
 * <p>
 * It provides a basic implementation for initializing and serializing such a
 * packet.
 *
 * @author Sebastian Staudt
 * @see RCONPacketFactory
 */
abstract public class RCONPacket extends SteamPacket
{
    /**
     * Header for authentication requests
     */
    public static final byte SERVERDATA_AUTH = 3;

    /**
     * Header for replies to authentication attempts
     */
    public static final byte SERVERDATA_AUTH_RESPONSE = 2;

    /**
     * Header for command execution requests
     */
    public static final byte SERVERDATA_EXECCOMMAND = 2;

    /**
     * Header for packets with the output of a command execution
     */
    public static final byte SERVERDATA_RESPONSE_VALUE = 0;

    /**
     * The packet header specifying the packet type
     */
    protected int header;

    /**
     * The request ID used to identify the RCON communication
     */
    protected int requestId;

    /**
     * Creates a new RCON packet object with the given request ID, type and
     * content data
     *
     * @param requestId The request ID for the current RCON communication
     * @param rconHeader The header for the packet type
     * @param rconData The raw packet data
     */
    protected RCONPacket(int requestId, int rconHeader, String rconData)
    {
	super((byte) 0, (rconData + "\0\0").getBytes());

	this.header = rconHeader;
	this.requestId = requestId;
    }

    /**
     * Returns the raw data representing this packet
     *
     * @return A byte array containing the raw data of this RCON packet
     */
    public byte[] getBytes()
    {
	byte[] bytes = new byte[this.contentData.getLength() + 12];

	System.arraycopy(Helper.byteArrayFromInteger(Integer.reverseBytes(bytes.length - 4)), 0, bytes, 0, 4);
	System.arraycopy(Helper.byteArrayFromInteger(Integer.reverseBytes(this.requestId)), 0, bytes, 4, 4);
	System.arraycopy(Helper.byteArrayFromInteger(Integer.reverseBytes(this.header)), 0, bytes, 8, 4);
	System.arraycopy(this.contentData.array(), 0, bytes, 12, bytes.length - 12);

	return bytes;
    }

    /**
     * Returns the request ID used to identify the RCON communication
     *
     * @return The request ID used to identify the RCON communication
     */
    public int getRequestId()
    {
	return this.requestId;
    }

}
