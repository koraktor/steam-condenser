/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2010, Sebastian Staudt
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
 * Defines common methods for sockets used to connect to game and master
 * servers.
 * @author Sebastian Staudt
 */
abstract public class SteamSocket
{
    private static int timeout = 1000;

    protected ByteBuffer buffer;
    protected SelectableChannel channel;
    protected InetSocketAddress remoteSocket;

    /**
     * Sets the timeout for socket operations. This usually only affects
     * timeouts, i.e. when a server does not respond in time.
     *
     * Due to the server-side implementation of the RCON protocol, each RCON
     * request will also wait this amount of time after execution. So if you
     * need RCON requests to execute fast, you should set this to a adequatly
     * low value.
     *
     * @param timeout The amount of milliseconds before a request times out
     */
    public static void setTimeout(int timeout) {
        SteamSocket.timeout = timeout;
    }

    /**
     * Creates a new SteamSocket used to connect to the given IP address and
     * port number
     * @param ipAddress The IP of the server to connect to
     * @param portNumber The port number of the server
     */
    protected SteamSocket(InetAddress ipAddress, int portNumber)
    {
        this.buffer = ByteBuffer.allocate(1400);
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);

        this.remoteSocket = new InetSocketAddress(ipAddress, portNumber);
    }

    /**
     * Reads a single packet from the buffer into a SteamPacket object
     * @return The SteamPacket object created from the data in the buffer
     * @throws com.github.koraktor.steamcondenser.exceptions.PacketFormatException When the SteamPacket could not be created
     *         because of an format error
     */
    protected SteamPacket getPacketFromData()
            throws PacketFormatException
    {
        byte[] packetData = new byte[this.buffer.remaining()];
        this.buffer.get(packetData);

        return SteamPacketFactory.getPacketFromData(packetData);
    }

    /**
     * Subclasses have to implement this method for their individual packet
     * format
     * @return The SteamPacket received
     * @throws IOException
     * @throws Exception
     */
    abstract public SteamPacket getReply()
            throws IOException, TimeoutException, SteamCondenserException;

    /**
     * Reads an UDP packet into an existing or a new buffer
     * @param bufferLength The length of the new buffer to created or 0 to use
     *        the existing buffer
     * @return The number of bytes received
     * @throws IOException
     * @throws TimeoutException
     * TODO select() seems to "miss" some packets (e.g. long RCON responses)
     */
    protected int receivePacket(int bufferLength)
            throws IOException, TimeoutException
    {
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
     * Closes the underlying {@link java.nio.channels.DatagramChannel}
     *
     * @see SelectableChannel#close
     */
    public void close() {
        try {
            this.channel.close();
        } catch (IOException e) {}
    }
}
