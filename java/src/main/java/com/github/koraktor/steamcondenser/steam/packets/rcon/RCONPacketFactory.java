/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets.rcon;

import com.github.koraktor.steamcondenser.PacketBuffer;
import com.github.koraktor.steamcondenser.exceptions.PacketFormatException;

/**
 * This module provides functionality to handle raw packet data for Source RCON
 *
 * It's is used to transform data bytes into packet objects for RCON
 * communication with Source servers.
 *
 * @author Sebastian Staudt
 * @see RCONPacket
 */
public abstract class RCONPacketFactory {

    /**
     * Creates a new packet object based on the header byte of the given raw
     * data
     *
     * @param rawData The raw data of the packet
     * @return RCONPacket The packet object generated from the packet data
     * @throws PacketFormatException if the packet header is not recognized
     */
    public static RCONPacket getPacketFromData(byte[] rawData)
            throws PacketFormatException {
        PacketBuffer packetBuffer = new PacketBuffer(rawData);

        int requestId = Integer.reverseBytes(packetBuffer.getInt());
        int header = Integer.reverseBytes(packetBuffer.getInt());
        String data = packetBuffer.getString();

        switch(header) {
            case RCONPacket.SERVERDATA_AUTH_RESPONSE:
                return new RCONAuthResponse(requestId);
            case RCONPacket.SERVERDATA_RESPONSE_VALUE:
                return new RCONExecResponsePacket(requestId, data);
            default:
                throw new PacketFormatException("Unknown packet with header " + header + " received.");
        }
    }
}
