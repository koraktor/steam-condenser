/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

/**
 * The S2A_LOGSTRING packet type is used to transfer log messages.
 *
 * @author Sebastian Staudt
 */
public class S2A_LOGSTRING_Packet extends SteamPacket
{

    /**
     * The log message contained in this packet
     */
    private String message;

    /**
     * Creates a new log message packet
     *
     * @param data
     */
    public S2A_LOGSTRING_Packet(byte[] data) {
        super(SteamPacket.S2A_LOGSTRING_HEADER, data);

        this.contentData.getByte();
        this.message = this.contentData.getString();
    }

    /**
     * @return The log message of this packet
     */
    public String getMessage() {
        return this.message;
    }

}
