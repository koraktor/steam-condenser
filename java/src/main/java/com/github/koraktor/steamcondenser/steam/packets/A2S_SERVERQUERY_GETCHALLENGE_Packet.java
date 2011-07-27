/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

/**
 * This packet class represents a A2S_SERVERQUERY_GETCHALLENGE request send to
 * a game server
 * <p>
 * It is used to retrieve a challenge number from the game server, which helps
 * to identify the requesting client.
 *
 * @author Sebastian Staudt
 * @see com.github.koraktor.steamcondenser.steam.servers.GameServer#updateChallengeNumber()
 */
public class A2S_SERVERQUERY_GETCHALLENGE_Packet extends SteamPacket
{
    /**
     * Creates a new A2S_SERVERQUERY_GETCHALLENGE request object
     */
    public A2S_SERVERQUERY_GETCHALLENGE_Packet()
    {
        super(SteamPacket.A2S_SERVERQUERY_GETCHALLENGE_HEADER);
    }
}
