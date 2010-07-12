/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @author Sebastian Staudt
 */
public abstract class S2A_INFO_BasePacket extends SteamPacket
{
    protected byte dedicated;
    protected String gameDescription;
    protected String gameDir;
    protected String mapName;
    protected int maxPlayers;
    protected int networkVersion;
    protected int numberOfBots;
    protected int numberOfPlayers;
    protected byte operatingSystem;
    protected boolean passwordProtected;
    protected boolean secure;
    protected String serverName;

    S2A_INFO_BasePacket(byte headerByte, byte[] dataBytes)
    {
	super(headerByte, dataBytes);
    }

    public HashMap<String, Object> getInfoHash()
    {
	HashMap<String, Object> infoHash = new HashMap<String, Object>();

	try
	{
	    for(Field field : this.getClass().getSuperclass().getDeclaredFields())
	    {
		infoHash.put(field.getName(), field.get(this));
	    }

	    for(Field field : this.getClass().getDeclaredFields())
	    {
		infoHash.put(field.getName(), field.get(this));
	    }
	}
	catch(IllegalAccessException e){}

	return infoHash;
    }
}
