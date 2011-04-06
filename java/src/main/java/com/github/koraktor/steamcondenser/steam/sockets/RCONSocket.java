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
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.github.koraktor.steamcondenser.exceptions.RCONBanException;
import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.packets.rcon.RCONPacket;
import com.github.koraktor.steamcondenser.steam.packets.rcon.RCONPacketFactory;

/**
 * @author Sebastian Staudt
 */
public class RCONSocket extends SteamSocket
{

    public RCONSocket(InetAddress ipAddress, int portNumber)
            throws IOException
    {
        super(ipAddress, portNumber);
        this.channel = SocketChannel.open();
    }

    /**
     * Closes the underlying {@link SocketChannel}
     *
     * @see SteamSocket#close
     */
    @Override
    public void close() {
        if(((SocketChannel)this.channel).isConnected()) {
            super.close();
        }
    }

    public void send(RCONPacket dataPacket)
            throws IOException
    {
        if(!((SocketChannel)this.channel).isConnected()) {
            ((SocketChannel)this.channel).connect(this.remoteSocket);
            this.channel.configureBlocking(false);
        }

        this.buffer = ByteBuffer.wrap(dataPacket.getBytes());
        ((SocketChannel)this.channel).write(this.buffer);
    }

    public RCONPacket getReply()
            throws IOException, TimeoutException, SteamCondenserException
    {
        if(this.receivePacket(4) <= 0) {
            throw new RCONBanException();
        }
        int packetSize = Integer.reverseBytes(this.buffer.getInt());
        int remainingBytes = packetSize;

        byte[] packetData;
        List<Byte> packetDataList = new ArrayList<Byte>();
        int receivedBytes;
        do {
            receivedBytes = this.receivePacket(remainingBytes);

            packetData = this.buffer.array();
            for(int i = 0; i < this.buffer.limit(); i ++){
                packetDataList.add(packetData[i]);
            }
            remainingBytes -= receivedBytes;
        } while(remainingBytes > 0);

        packetData = new byte[packetDataList.size()];
        for(int i = 0; i < packetData.length; i ++) {
            packetData[i] = packetDataList.get(i);
        }

        return RCONPacketFactory.getPacketFromData(packetData);
    }
}
