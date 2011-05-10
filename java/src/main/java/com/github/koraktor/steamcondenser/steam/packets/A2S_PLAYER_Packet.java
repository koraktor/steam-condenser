/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

import com.github.koraktor.steamcondenser.Helper;

/**
 * This packet class represents a A2S_PLAYER request send to a game server
 * <p>
 * It is used to request the list of players currently playing on the server.
 * <p>
 * This packet type requires the client to challenge the server in advance,
 * which is done automatically if required.
 *
 * @author Sebastian Staudt
 * @see com.github.koraktor.steamcondenser.steam.servers.GameServer#updatePlayerInfo()
 */
public class A2S_PLAYER_Packet extends SteamPacket
{

    /**
     * Creates a new A2S_PLAYER request object including the challenge number
     *
     * @param challengeNumber The challenge number received from the server
     */
    public A2S_PLAYER_Packet(int challengeNumber)
    {
	super(SteamPacket.A2S_PLAYER_HEADER, Helper.byteArrayFromInteger(Integer.reverseBytes(challengeNumber)));
    }
}
