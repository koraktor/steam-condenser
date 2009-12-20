/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package steamcondenser;

/**
 * @author Sebastian Staudt
 */
public class RCONNoAuthException extends SteamCondenserException
{
    private static final long serialVersionUID = 6303597320774324157L;

    public RCONNoAuthException()
    {
	super("Not authenticated yet.");
    }
}
