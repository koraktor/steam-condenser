/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */
package steamcondenser.steam.sockets;

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

import steamcondenser.PacketFormatException;
import steamcondenser.SteamCondenserException;
import steamcondenser.steam.packets.SteamPacket;
import steamcondenser.steam.packets.SteamPacketFactory;

/**
 * Defines common methods for sockets used to connect to game and master
 * servers.
 * @author Sebastian Staudt
 * @version $Id$
 */
abstract public class SteamSocket
{
    protected ByteBuffer buffer;
    protected SelectableChannel channel;
    protected InetSocketAddress remoteSocket;

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
     * @throws PacketFormatException When the SteamPacket could not be created
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

        if (selector.select(1000) == 0) {
            throw new TimeoutException();
        }

        int bytesRead;

        if (bufferLength == 0) {
            this.buffer.clear();
        } else {
            this.buffer = ByteBuffer.allocate(bufferLength);
        }

        bytesRead = ((ReadableByteChannel) this.channel).read(this.buffer);
        this.buffer.rewind();
        this.buffer.limit(bytesRead);

        selector.close();

        return bytesRead;
    }

    /**
     * Closes the DatagramChannel
     */
    @Override
    public void finalize()
            throws IOException
    {
        this.channel.close();
    }
}
