/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

import com.github.koraktor.steamcondenser.Helper;

/**
 * This packet class represents a A2S_RULES request send to a game server
 * <p>
 * The game server will return a list of currently active game rules, e.g.
 * <code>mp_friendlyfire = 1</code>.
 * <p>
 * This packet type requires the client to challenge the server in advance,
 * which is done automatically if required.
 *
 * @author Sebastian Staudt
 * @see com.github.koraktor.steamcondenser.steam.servers.GameServer#updateRulesInfo()
 */
public class A2S_RULES_Packet extends SteamPacket
{

    /**
     * Creates a new A2S_RULES request object including the challenge number
     *
     * @param challengeNumber The challenge number received from the server
     */
    public A2S_RULES_Packet(int challengeNumber)
    {
        super(SteamPacket.A2S_RULES_HEADER, Helper.byteArrayFromInteger(Integer.reverseBytes(challengeNumber)));
    }
}
