/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.exceptions;

/**
 * @author Sebastian Staudt
 */
public class RCONNoAuthException extends SteamCondenserException
{
    public RCONNoAuthException() {
        super("Not authenticated yet.");
    }
}
