/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008, Sebastian Staudt
 */

package steamcondenser;

public class NothingReceivedException extends SteamCondenserException
{
    private static final long serialVersionUID = 4147705099294124283L;

    public NothingReceivedException()
    {
	super("No data received.");
    }
}
