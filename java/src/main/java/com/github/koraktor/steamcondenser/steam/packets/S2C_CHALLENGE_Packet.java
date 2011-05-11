/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

/**
 * This packet class represents a S2C_CHALLENGE response replied by a game
 * server
 *
 * It is used to provide a challenge number to a client requesting information
 * from the game server.
 *
 * @author Sebastian Staudt
 * @see com.github.koraktor.steamcondenser.steam.servers.GameServer#updateChallengeNumber
 */
public class S2C_CHALLENGE_Packet extends SteamPacket
{
    /**
     * Creates a new S2C_CHALLENGE response object based on the given data
     *
     * @param challengeNumberBytes The raw packet data replied from the server
     */
    public S2C_CHALLENGE_Packet(byte[] challengeNumberBytes)
    {
	super(SteamPacket.S2C_CHALLENGE_HEADER, challengeNumberBytes);
    }

    /**
     * Returns the challenge number received from the game server
     *
     * @return The challenge number provided by the game server
    */
    public int getChallengeNumber()
    {
	return Integer.reverseBytes(this.contentData.getInt());
    }
}
