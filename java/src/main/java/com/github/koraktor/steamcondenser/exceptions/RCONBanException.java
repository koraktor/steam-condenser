/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009-2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.exceptions;

/**
 * This exception class indicates that the IP address your accessing the game
 * server from has been banned by the server
 * <p>
 * You or the server operator will have to unban your IP address on the server.
 *
 * @author Sebastian Staudt
 * @see com.github.koraktor.steamcondenser.steam.servers.GameServer#rconAuth
 */
public class RCONBanException extends SteamCondenserException {

    /**
     * Creates a new <code>RCONBanException</code> instance
     */
    public RCONBanException() {
        super("You have been banned from this server.");
    }
}
