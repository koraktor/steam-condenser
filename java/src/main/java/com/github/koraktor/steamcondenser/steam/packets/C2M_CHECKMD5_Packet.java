/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

/**
 * The C2M_CHECKMD5 packet type is used to initialize (challenge) master server
 * communication.
 *
 * @author Sebastian Staudt
 */
public class C2M_CHECKMD5_Packet extends SteamPacket
{

    /**
     * Creates a new challenge request packet for master server communication
     */
    public C2M_CHECKMD5_Packet() {
        super(SteamPacket.C2M_CHECKMD5_HEADER);
    }

    public byte[] getBytes() {
        byte[] bytes = new byte[2];
        bytes[0] = this.headerData;
        bytes[1] = (byte) 0xFF;

        return bytes;
    }

}
