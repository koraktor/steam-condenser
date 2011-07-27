/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

/**
 * This packet class represents a M2S_ISVALIDMD5 response replied by a master
 * server
 * <p>
 * It is used to provide a challenge number to a game server
 *
 * @author Sebastian Staudt
 * @see    com.github.koraktor.steamcondenser.steam.servers.MasterServer#getChallenge
 */
public class M2C_ISVALIDMD5_Packet extends SteamPacket {

    private int challenge;

    /**
     * Creates a new M2S_ISVALIDMD5 response object based on the given data
     *
     * @param data The raw packet data replied from the server
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
