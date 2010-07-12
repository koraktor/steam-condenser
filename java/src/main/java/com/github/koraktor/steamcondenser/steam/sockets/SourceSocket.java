/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.packets.SteamPacket;
import com.github.koraktor.steamcondenser.steam.packets.SteamPacketFactory;

/**
 * A socket used for connections to Source game servers.
 * @author Sebastian Staudt
 */
public class SourceSocket extends QuerySocket
{
    public SourceSocket(InetAddress ipAddress, int portNumber)
            throws IOException
    {
        super(ipAddress, portNumber);
    }

    public SteamPacket getReply()
            throws IOException, TimeoutException, SteamCondenserException
    {
        int bytesRead;
        boolean isCompressed = false;
        SteamPacket packet;

        bytesRead = this.receivePacket(1400);

        if(this.packetIsSplit()) {
            byte[] splitData;
            int packetCount, packetNumber, requestId, splitSize;
            int packetChecksum = 0;
            ArrayList<byte[]> splitPackets = new ArrayList<byte[]>();

            do {
                // Parsing of split packet headers
                requestId = Integer.reverseBytes(this.buffer.getInt());
                isCompressed = ((requestId & 0x80000000) != 0);
                packetCount = this.buffer.get();
                packetNumber = this.buffer.get() + 1;

                if(isCompressed) {
                    splitSize = Integer.reverseBytes(this.buffer.getInt());
                    packetChecksum = Integer.reverseBytes(this.buffer.getInt());
                }
                else {
                    splitSize = Short.reverseBytes(this.buffer.getShort());
                }

                // Caching of split packet Data
                splitData = new byte[Math.min(splitSize, this.buffer.remaining())];
                this.buffer.get(splitData);
                splitPackets.ensureCapacity(packetCount);
                splitPackets.add(packetNumber - 1, splitData);

                // Receiving the next packet
                if(splitPackets.size() < packetCount) {
                    try {
                        bytesRead = this.receivePacket();
                    }
                    catch(TimeoutException e) {
                        bytesRead = 0;
                    }
                }
                else {
                    bytesRead = 0;
                }

                Logger.getLogger("global").info("Received packet #" + packetNumber + " of " + packetCount + " for request ID " + requestId + ".");
            } while(bytesRead > 0 && this.packetIsSplit());

            if(isCompressed) {
                packet = SteamPacketFactory.reassemblePacket(splitPackets, true, splitSize, packetChecksum);
            }
            else {
                packet = SteamPacketFactory.reassemblePacket(splitPackets);
            }
        }
        else {
            packet = this.getPacketFromData();
        }

        this.buffer.flip();

        if(isCompressed) {
            Logger.getLogger("global").info("Received compressed reply of type \"" + packet.getClass().getSimpleName() + "\"");
        }
        else {
            Logger.getLogger("global").info("Received reply of type \"" + packet.getClass().getSimpleName() + "\"");
        }

        return packet;
    }
}
