/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

/**
 * This packet class represent a M2S_REQUESTRESTART response replied from a
 * master server
 * <p>
 * It is used to request a game server restart, e.g. when the server is
 * outdated.
 *
 * @author Sebastian Staudt
 * @see com.github.koraktor.steamcondenser.steam.servers.MasterServer#sendHeartbeat
 */
public class M2S_REQUESTRESTART_Packet extends SteamPacket {

    private int challenge;

    /**
     * Creates a new M2S_REQUESTRESTART object based on the given data
     *
     * @param data The raw packet data replied from the server
     */
    public M2S_REQUESTRESTART_Packet(byte[] data) {
        super(SteamPacket.C2M_CHECKMD5_HEADER, data);

        this.challenge = this.contentData.getInt();
    }

    /**
     * Returns the challenge number used for master server communication
     *
     * @return The challenge number
     */
    public int getChallenge() {
        return this.challenge;
    }

}
