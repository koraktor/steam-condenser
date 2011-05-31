/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets.rcon;

/**
 * This packet class represents a SERVERDATA_RESPONSE_VALUE packet sent by a
 * Source server
 * <p>
 * It is used to transport the output of a command from the server to the
 * client which requested the command execution.
 *
 * @author Sebastian Staudt
 * @see com.github.koraktor.steamcondenser.steam.servers.SourceServer#rconExec
 */
public class RCONExecResponsePacket extends RCONPacket
{
    /**
     * Creates a RCON command response for the given request ID and command
     * output
     *
     * @param requestId The request ID of the RCON connection
     * @param commandReturn The output of the command executed on the server
     */
    public RCONExecResponsePacket(int requestId, String commandReturn)
    {
	super(requestId, RCONPacket.SERVERDATA_RESPONSE_VALUE, commandReturn);
    }

    /**
     * Returns the output of the command execution
     *
     * @return The output of the command
     */
    public String getResponse()
    {
        String response = new String(this.contentData.array());
	return response.substring(0, response.length() - 2);
    }
}
