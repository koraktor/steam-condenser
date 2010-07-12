/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import com.github.koraktor.steamcondenser.steam.packets.SteamPacket;

public abstract class QuerySocket extends SteamSocket
{
    protected QuerySocket(InetAddress ipAddress, int portNumber)
            throws IOException
    {
        super(ipAddress, portNumber);
        this.channel = DatagramChannel.open();
        this.channel.configureBlocking(false);
        ((DatagramChannel) this.channel).connect(this.remoteSocket);
    }

    /**
     * Checks whether a packet in the buffer is split or not
     * @return true if the packet is split, otherwise false
     */
    protected boolean packetIsSplit()
    {
        return (Integer.reverseBytes(this.buffer.getInt()) == 0xFFFFFFFE);
    }

    /**
     * Reads an UDP packet into an existing buffer
     * @return The number of bytes received
     * @throws IOException
     * @throws TimeoutException
     */
    protected int receivePacket()
            throws IOException, TimeoutException
    {
        return this.receivePacket(0);
    }

    /**
     * Sends a SteamPacket object over the UDP channel to the remote end
     * @param dataPacket The {@link com.github.koraktor.steamcondenser.steam.packets.SteamPacket
     *        SteamPacket} to send to the remote end
     */
    public void send(SteamPacket dataPacket)
            throws IOException
    {
        Logger.getLogger("global").info("Sending data packet of type \"" + dataPacket.getClass().getSimpleName() + "\"");

        this.buffer = ByteBuffer.wrap(dataPacket.getBytes());
        ((DatagramChannel) this.channel).send(this.buffer, this.remoteSocket);
        this.buffer.flip();
    }
}
