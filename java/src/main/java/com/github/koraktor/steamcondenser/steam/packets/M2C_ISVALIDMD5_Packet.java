/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

/**
 * A packet of type M2S_ISVALIDMD5 is used by the master server to provide a
 * challenge number to a game server
 *
 * @author Sebastian Staudt
 */
public class M2C_ISVALIDMD5_Packet extends SteamPacket
{

    private int challenge;

    /**
     * Creates a new response packet with the data from the master server
     *
     * @param data The packet data sent by the master server
     */
    public M2C_ISVALIDMD5_Packet(byte[] data) {
        super(SteamPacket.M2C_ISVALIDMD5_HEADER, data);

        this.contentData.getByte();
        this.challenge = Integer.reverseBytes(this.contentData.getInt());
    }

    /**
     * Returns the challenge number to use for master server communication
     *
     * @return The challenge number
     */
    public int getChallenge() {
        return this.challenge;
    }
}
