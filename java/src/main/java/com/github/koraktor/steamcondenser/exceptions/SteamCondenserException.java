/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.exceptions;

/**
 * This exception class is used as a base class for all exceptions related to
 * Steam Condenser's operation
 *
 * @author Sebastian Staudt
 */
public class SteamCondenserException extends Exception
{
    /**
     * Creates a new <code>SteamCondenserException</code> instance
     */
    public SteamCondenserException() {}

    /**
     * Creates a new <code>SteamCondenserException</code> instance
     *
     * @param message The message to attach to the exception
     */
    public SteamCondenserException(String message) {
        super(message);
    }
}
