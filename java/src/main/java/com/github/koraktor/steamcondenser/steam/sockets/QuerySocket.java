/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.packets.SteamPacket;

/**
 * This class implements basic functionality for UDP based sockets
 *
 * @author Sebastian Staudt
 */
public abstract class QuerySocket extends SteamSocket {

    /**
     * Creates a new socket to communicate with the server on the given IP
     * address and port
     *
     * @param ipAddress Either the IP address or the DNS name of the server
     * @param portNumber The port the server is listening on
     * @throws SteamCondenserException if the socket cannot be opened
     */
    protected QuerySocket(InetAddress ipAddress, int portNumber)
            throws  SteamCondenserException {
        super(ipAddress, portNumber);

        try {
            this.channel = DatagramChannel.open();
            this.channel.configureBlocking(false);
            ((DatagramChannel) this.channel).connect(this.remoteSocket);
        } catch(IOException e) {
            throw new SteamCondenserException(e.getMessage(), e);
        }
    }

    /**
     * Returns whether a packet in the buffer is split
     *
     * @return <code>true</code> if the packet is split
     */
    protected boolean packetIsSplit() {
        return (Integer.reverseBytes(this.buffer.getInt()) == 0xFFFFFFFE);
    }

    /**
     * Reads an UDP packet into the buffer
     *
     * @return The number of bytes received
     * @throws SteamCondenserException if an error occurs while reading from
     *         the socket
     * @throws TimeoutException if no UDP packet was received
     */
    protected int receivePacket()
            throws SteamCondenserException, TimeoutException {
        return this.receivePacket(0);
    }

    /**
     * Sends the given packet to the server
     *
     * @param dataPacket The packet to send to the server
     * @throws SteamCondenserException if an error occurs while writing to the
     *         socket
     */
    public void send(SteamPacket dataPacket)
            throws SteamCondenserException {
        Logger.getLogger("global").info("Sending data packet of type \"" + dataPacket.getClass().getSimpleName() + "\"");

        try {
            this.buffer = ByteBuffer.wrap(dataPacket.getBytes());
            ((DatagramChannel) this.channel).send(this.buffer, this.remoteSocket);
            this.buffer.flip();
        } catch(IOException e) {
            throw new SteamCondenserException(e.getMessage(), e);
        }
    }
}
