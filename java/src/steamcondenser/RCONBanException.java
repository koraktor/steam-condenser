/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009, Sebastian Staudt
 */

package steamcondenser;

/**
 * @author Sebastian Staudt
 */
public class RCONBanException extends SteamCondenserException
{
    private static final long serialVersionUID = 6303597320774324157L;

    public RCONBanException()
    {
	super("You have been banned from this server.");
    }
}
