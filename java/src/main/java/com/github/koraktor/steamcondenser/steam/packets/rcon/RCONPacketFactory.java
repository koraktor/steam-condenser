/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets.rcon;

import com.github.koraktor.steamcondenser.PacketBuffer;
import com.github.koraktor.steamcondenser.exceptions.PacketFormatException;
import com.github.koraktor.steamcondenser.steam.packets.SteamPacketFactory;

public abstract class RCONPacketFactory extends SteamPacketFactory
{
    public static RCONPacket getPacketFromData(byte[] rawData)
        throws PacketFormatException
    {
        PacketBuffer packetBuffer = new PacketBuffer(rawData);

        int size = Integer.reverseBytes(packetBuffer.getInt());
        int requestId = Integer.reverseBytes(packetBuffer.getInt());
        int header = Integer.reverseBytes(packetBuffer.getInt());
        String data = packetBuffer.getString();

        if(size - 10 != data.length()) {
            return null;
        }

        switch(header)
        {
            case RCONPacket.SERVERDATA_AUTH_RESPONSE:
                return new RCONAuthResponse(requestId);
            case RCONPacket.SERVERDATA_RESPONSE_VALUE:
                return new RCONExecResponsePacket(requestId, data);
            default:
                throw new PacketFormatException("Unknown packet with header " + header + " received.");
        }
    }
}
