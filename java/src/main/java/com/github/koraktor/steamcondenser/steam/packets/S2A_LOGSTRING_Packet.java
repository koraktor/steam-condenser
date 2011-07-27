/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

/**
 * This class represents a S2A_LOGSTRING packet used to transfer log messages
 *
 * @author Sebastian Staudt
 */
public class S2A_LOGSTRING_Packet extends SteamPacket {

    /**
     * The log message contained in this packet
     */
    private String message;

    /**
     * Creates a new S2A_LOGSTRING object based on the given data
     *
     * @param data The raw packet data sent by the server
     */
    public S2A_LOGSTRING_Packet(byte[] data) {
        super(SteamPacket.S2A_LOGSTRING_HEADER, data);

        this.contentData.getByte();
        this.message = this.contentData.getString();
    }

    /**
     * Returns the log message contained in this packet
     *
     * @return The log message
     */
    public String getMessage() {
        return this.message;
    }

}
