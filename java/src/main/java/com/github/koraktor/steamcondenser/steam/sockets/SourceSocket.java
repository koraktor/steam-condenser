/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.sockets;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.packets.SteamPacket;
import com.github.koraktor.steamcondenser.steam.packets.SteamPacketFactory;

/**
 * This class represents a socket used to communicate with game servers based
 * on the Source engine (e.g. Team Fortress 2, Counter-Strike: Source)
 *
 * @author Sebastian Staudt
 */
public class SourceSocket extends QuerySocket {

    /**
     * Creates a new socket to communicate with the server on the given IP
     * address and port
     *
     * @param ipAddress Either the IP address or the DNS name of the server
     * @param portNumber The port the server is listening on
     * @throws SteamCondenserException if the socket cannot be opened
     */
    public SourceSocket(InetAddress ipAddress, int portNumber)
            throws SteamCondenserException {
        super(ipAddress, portNumber);
    }

    /**
     * Reads a packet from the socket
     * <p>
     * The Source query protocol specifies a maximum packet size of 1,400
     * bytes. Bigger packets will be split over several UDP packets. This
     * method reassembles split packets into single packet objects.
     * Additionally Source may compress big packets using bzip2. Those packets
     * will be compressed.
     *
     * @return SteamPacket The packet replied from the server
     * @throws SteamCondenserException if an error occurs while communicating
     *         with the server
     * @throws TimeoutException if the request times out
     */
    public SteamPacket getReply()
            throws SteamCondenserException, TimeoutException {
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
                requestId = Integer.reverseBytes(this.buffer.getInt());
                isCompressed = ((requestId & 0x80000000) != 0);
                packetCount = this.buffer.get();
                packetNumber = this.buffer.get() + 1;

                if(isCompressed) {
                    splitSize = Integer.reverseBytes(this.buffer.getInt());
                    packetChecksum = Integer.reverseBytes(this.buffer.getInt());
                } else {
                    splitSize = Short.reverseBytes(this.buffer.getShort());
                }

                splitData = new byte[Math.min(splitSize, this.buffer.remaining())];
                this.buffer.get(splitData);
                splitPackets.ensureCapacity(packetCount);
                splitPackets.add(packetNumber - 1, splitData);

                if(splitPackets.size() < packetCount) {
                    try {
                        bytesRead = this.receivePacket();
                    } catch(TimeoutException e) {
                        bytesRead = 0;
                    }
                } else {
                    bytesRead = 0;
                }

                Logger.getLogger("com.github.koraktor.steamcondenser").info("Received packet #" + packetNumber + " of " + packetCount + " for request ID " + requestId + ".");
            } while(bytesRead > 0 && this.packetIsSplit());

            if(isCompressed) {
                packet = SteamPacketFactory.reassemblePacket(splitPackets, true, splitSize, packetChecksum);
            } else {
                packet = SteamPacketFactory.reassemblePacket(splitPackets);
            }
        } else {
            packet = this.getPacketFromData();
        }

        this.buffer.flip();

        if(isCompressed) {
            Logger.getLogger("com.github.koraktor.steamcondenser").info("Received compressed reply of type \"" + packet.getClass().getSimpleName() + "\"");
        } else {
            Logger.getLogger("com.github.koraktor.steamcondenser").info("Received reply of type \"" + packet.getClass().getSimpleName() + "\"");
        }

        return packet;
    }
}
