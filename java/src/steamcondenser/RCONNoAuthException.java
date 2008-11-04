/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class RCONNoAuthException extends SteamCondenserException
{
    private static final long serialVersionUID = 6303597320774324157L;

    public RCONNoAuthException()
    {
	super("Not authenticated yet.");
    }
}
