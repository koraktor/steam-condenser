/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets.rcon;

/**
 * This class is used to determine the end of a RCON response from Source
 * servers. Packets of this type are sent after the actual RCON command and the
 * empty response packet from the server will indicate the end of the response.
 *
 * @author Sebastian Staudt
 */
public class RCONTerminator extends RCONPacket {

    /**
     * Creates a new RCONTerminator instance for the given request ID
     *
     * @param requestId The request ID for this RCON session
     */
    public RCONTerminator(int requestId) {
        super(requestId, RCONPacket.SERVERDATA_RESPONSE_VALUE, "");
    }

}
