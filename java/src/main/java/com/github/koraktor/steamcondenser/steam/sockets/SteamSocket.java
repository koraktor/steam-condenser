/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.concurrent.TimeoutException;

import com.github.koraktor.steamcondenser.exceptions.PacketFormatException;
import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.packets.SteamPacket;
import com.github.koraktor.steamcondenser.steam.packets.SteamPacketFactory;

/**
 * This abstract class implements common functionality for sockets used to
 * connect to game and master servers
 *
 * @author Sebastian Staudt
 */
abstract public class SteamSocket {

    private static int timeout = 1000;

    protected ByteBuffer buffer;
    protected SelectableChannel channel;
    protected InetSocketAddress remoteSocket;

    /**
     * Sets the timeout for socket operations
     * <p>
     * Any request that takes longer than this time will cause a {@link
     * TimeoutException}.
     *
     * @param timeout The amount of milliseconds before a request times out
     */
    public static void setTimeout(int timeout) {
        SteamSocket.timeout = timeout;
    }

    /**
     * Creates a new UDP socket to communicate with the server on the given IP
     * address and port
     *
     * @param ipAddress Either the IP address or the DNS name of the server
     * @param portNumber The port the server is listening on
     */
    protected SteamSocket(InetAddress ipAddress, int portNumber) {
        this.buffer = ByteBuffer.allocate(1400);
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);

        this.remoteSocket = new InetSocketAddress(ipAddress, portNumber);
    }

    /**
     * Reads a single packet from the buffer into a packet object
     *
     * @return The packet object created from the data in the buffer
     * @throws PacketFormatException if the data is not formatted correctly
     */
    protected SteamPacket getPacketFromData()
            throws PacketFormatException {
        byte[] packetData = new byte[this.buffer.remaining()];
        this.buffer.get(packetData);

        return SteamPacketFactory.getPacketFromData(packetData);
    }

    /**
     * Subclasses have to implement this method for their individual packet
     * formats
     *
     * @return The packet replied from the server
     * @throws IOException if an error occurs while communicating with the
     *         server
     * @throws SteamCondenserException if the reply cannot be parsed
     * @throws TimeoutException if the request times out
     */
    abstract public SteamPacket getReply()
            throws IOException, TimeoutException, SteamCondenserException;

    /**
     * Reads the given amount of data from the socket and wraps it into the
     * buffer
     *
     * @param bufferLength The data length to read from the socket
     * @throws TimeoutException if no packet is received on time
     * @return int The number of bytes that have been read from the socket
     * @see ByteBuffer
     */
    protected int receivePacket(int bufferLength)
            throws IOException, TimeoutException {
        Selector selector = Selector.open();
        this.channel.register(selector, SelectionKey.OP_READ);

        if (selector.select(SteamSocket.timeout) == 0) {
            selector.close();
            throw new TimeoutException();
        }

        int bytesRead;

        if (bufferLength == 0) {
            this.buffer.clear();
        } else {
            this.buffer = ByteBuffer.allocate(bufferLength);
        }

        bytesRead = ((ReadableByteChannel) this.channel).read(this.buffer);
        if(bytesRead > 0) {
            this.buffer.rewind();
            this.buffer.limit(bytesRead);
        }

        selector.close();

        return bytesRead;
    }

    /**
     * Closes this socket
     *
     * @see #close
     */
    @Override
    public void finalize()
            throws IOException {
        this.close();
    }

    /**
     * Closes the underlying socket
     *
     * @see SelectableChannel#close
     */
    public void close() {
        try {
            this.channel.close();
        } catch (IOException e) {}
    }
}
