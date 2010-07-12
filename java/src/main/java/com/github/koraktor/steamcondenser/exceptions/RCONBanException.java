/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009-2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.exceptions;

/**
 * @author Sebastian Staudt
 */
public class RCONBanException extends SteamCondenserException
{
    public RCONBanException() {
        super("You have been banned from this server.");
    }
}
