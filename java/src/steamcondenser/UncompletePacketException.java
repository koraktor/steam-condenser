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
public class UncompletePacketException extends SteamCondenserException
{
    private static final long serialVersionUID = 5524570166078828173L;

    /**
     * Creates an UncompletePacketException
     */
    public UncompletePacketException()
    {
	super("Packet is missing a part of data.");
    }
}
