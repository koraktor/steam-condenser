/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

import java.util.HashMap;
import com.github.koraktor.steamcondenser.exceptions.PacketFormatException;
import com.github.koraktor.steamcondenser.steam.SteamPlayer;

/**
 * @author Sebastian Staudt
 */
public class S2A_PLAYER_Packet extends SteamPacket
{
    private HashMap<String, SteamPlayer> playerHash;

    public S2A_PLAYER_Packet(byte[] dataBytes)
    throws PacketFormatException
    {
	super(SteamPacket.S2A_PLAYER_HEADER, dataBytes);

	if(this.contentData.getLength() == 0)
	{
	    throw new PacketFormatException("Wrong formatted A2A_PLAYER response packet.");
	}

	this.playerHash = new HashMap<String, SteamPlayer>(this.contentData.getByte());

	while(this.contentData.hasRemaining())
	{
		byte playerId = this.contentData.getByte();
		String playerName = this.contentData.getString();
	    this.playerHash.put(playerName, new SteamPlayer(
		    playerId,
		    playerName,
		    Integer.reverseBytes(this.contentData.getInt()),
		    Float.intBitsToFloat(Integer.reverseBytes(this.contentData.getInt()))
	    ));
	}
    }

    public HashMap<String, SteamPlayer> getPlayerHash()
	{
		return this.playerHash;
    }
}
