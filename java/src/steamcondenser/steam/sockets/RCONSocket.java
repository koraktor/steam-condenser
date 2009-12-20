/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package steamcondenser.steam.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeoutException;

import steamcondenser.RCONBanException;
import steamcondenser.SteamCondenserException;
import steamcondenser.steam.packets.rcon.RCONPacket;
import steamcondenser.steam.packets.rcon.RCONPacketFactory;

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

    public void send(RCONPacket dataPacket)
            throws IOException
    {
        if( ! ((SocketChannel)this.channel).isConnected()) {
            ((SocketChannel)this.channel).connect(this.remoteSocket);
            this.channel.configureBlocking(false);
        }

        this.buffer = ByteBuffer.wrap(dataPacket.getBytes());
        ((SocketChannel)this.channel).write(this.buffer);
    }

    public RCONPacket getReply()
            throws IOException, TimeoutException, SteamCondenserException
    {
        if(this.receivePacket(1440) <= 0) {
            throw new RCONBanException();
        }
        String packetData = new String(this.buffer.array()).substring(0, this.buffer.limit());
        int packetSize = Integer.reverseBytes(this.buffer.getInt()) + 4;

        if(packetSize > 1440) {
            int remainingBytes = packetSize - this.buffer.limit();
            do {
                if(remainingBytes < 1440) {
                    this.receivePacket(remainingBytes);
                }
                else {
                    this.receivePacket(1440);
                }
                packetData += new String(this.buffer.array()).substring(0, this.buffer.limit());
                remainingBytes -= this.buffer.limit();
            } while(remainingBytes > 0);
        }

        return RCONPacketFactory.getPacketFromData(packetData.getBytes());
    }
}
