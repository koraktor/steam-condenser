/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.exceptions;

/**
 * This exception class indicates a problem when parsing packet data from the
 * responses received from a game or master server
 *
 * @author Sebastian Staudt
 */
public class PacketFormatException extends SteamCondenserException
{

    /**
     * Creates a new <code>PacketFormatException</code> instance
     *
     * @param message The message to attach to the exception
     */
    public PacketFormatException(String message) {
        super(message);
    }

}
