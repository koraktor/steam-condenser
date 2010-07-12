/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets.rcon;

public class RCONExecRequestPacket extends RCONPacket
{
    public RCONExecRequestPacket(int requestId, String rconCommand)
    {
	super(requestId, RCONPacket.SERVERDATA_EXECCOMMAND, rconCommand);
    }
}
