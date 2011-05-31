/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets.rcon;

/**
 * This packet class represents a SERVERDATA_EXECCOMMAND packet sent to a
 * Source server
 * <p>
 * It is used to request a command execution on the server.
 *
 * @author Sebastian Staudt
 * @see com.github.koraktor.steamcondenser.steam.servers.SourceServer#rconExec
 */
public class RCONExecRequestPacket extends RCONPacket
{
    /**
     * Creates a RCON command execution request for the given request ID and
     * command
     *
     * @param requestId The request ID of the RCON connection
     * @param rconCommand The command to execute on the server
     */
    public RCONExecRequestPacket(int requestId, String rconCommand)
    {
	super(requestId, RCONPacket.SERVERDATA_EXECCOMMAND, rconCommand);
    }
}
