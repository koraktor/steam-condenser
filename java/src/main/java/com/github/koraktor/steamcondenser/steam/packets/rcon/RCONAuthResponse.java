/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets.rcon;

/**
 * This packet class represents a SERVERDATA_AUTH_RESPONSE packet sent by a
 * Source server
 * <p>
 * It is used to indicate the success or failure of an authentication attempt
 * of a client for RCON communication.
 *
 * @author Sebastian Staudt
 * @see com.github.koraktor.steamcondenser.steam.servers.SourceServer#rconAuth
 */
public class RCONAuthResponse extends RCONPacket
{
    /**
     * Creates a RCON authentication response for the given request ID
     * <p>
     * The request ID of the packet will match the client's request if
     * authentication was successful
     *
     * @param requestId The request ID of the RCON connection
     */
    public RCONAuthResponse(int requestId)
    {
	super(requestId, RCONPacket.SERVERDATA_AUTH_RESPONSE, "");
    }
}
