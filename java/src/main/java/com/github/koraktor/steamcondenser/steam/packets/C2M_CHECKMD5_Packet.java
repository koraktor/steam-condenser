/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

/**
 * This packet class represents a C2M_CHECKMD5 request sent to a master server
 * <p>
 * It is used to initialize (challenge) master server communication.
 *
 * @author Sebastian Staudt
 * @see com.github.koraktor.steamcondenser.steam.servers.MasterServer#getChallenge()
 */
public class C2M_CHECKMD5_Packet extends SteamPacket {

    /**
     * Creates a new C2M_CHECKMD% request object
     */
    public C2M_CHECKMD5_Packet() {
        super(SteamPacket.C2M_CHECKMD5_HEADER);
    }

    /**
     * Returns the raw data representing this packet
     *
     * @return A byte array containing the raw data of this request packet
     */
    public byte[] getBytes() {
        byte[] bytes = new byte[2];
        bytes[0] = this.headerData;
        bytes[1] = (byte) 0xFF;

        return bytes;
    }

}
