/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.community;

/**
 * The SteamGroup class represents a group in the Steam Community
 * @author Sebastian Staudt
 * @version $Id$
 */
public class SteamGroup
{
    private long id;

    public SteamGroup(long id)
    {
	this.id = id;
    }

    public long getId()
    {
	return this.id;
    }
}
