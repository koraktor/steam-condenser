/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets.rcon;

public class RCONExecResponsePacket extends RCONPacket
{
    public RCONExecResponsePacket(int requestId, String commandReturn)
    {
	super(requestId, RCONPacket.SERVERDATA_RESPONSE_VALUE, commandReturn);
    }

    public String getResponse()
    {
        String response = new String(this.contentData.array());
	return response.substring(0, response.length() - 2);
    }
}
