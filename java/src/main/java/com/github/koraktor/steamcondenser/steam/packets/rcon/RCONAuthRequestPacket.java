/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets.rcon;

/**
 * This packet class represents a SERVERDATA_AUTH request sent to a Source
 * server
 * <p>
 * It is used to authenticate the client for RCON communication.
 *
 * @author Sebastian Staudt
 * @see com.github.koraktor.steamcondenser.steam.servers.SourceServer#rconAuth
 */
public class RCONAuthRequestPacket extends RCONPacket {

    /**
     * Creates a RCON authentication request for the given request ID and RCON
     * password
     *
     * @param requestId The request ID of the RCON connection
     * @param rconPassword The RCON password of the server
     */
    public RCONAuthRequestPacket(int requestId, String rconPassword) {
        super(requestId, RCONPacket.SERVERDATA_AUTH, rconPassword);
    }
}
