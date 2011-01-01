/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

/**
 * The M2S_REQUESTRESTART packet type is used to by master servers to request a
 * game server restart, e.g. when using outdated versions.
 *
 * @author Sebastian Staudt
 */
public class M2S_REQUESTRESTART_Packet extends SteamPacket
{

    private int challenge;

    /**
     * Creates a new server restart request packet sent by a master server
     *
     * @param data This packet returns the challenge number initially
     *        provided by an M2C_ISVALIDMD5 packet.
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
    public int getChallenge()
    {
        return this.challenge;
    }

}
