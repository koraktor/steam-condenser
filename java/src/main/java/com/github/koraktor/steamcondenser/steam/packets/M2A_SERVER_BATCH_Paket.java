/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

import java.util.Vector;

import com.github.koraktor.steamcondenser.exceptions.PacketFormatException;

/**
 * This packet class represents a M2A_SERVER_BATCH response replied by a master
 * server
 * <p>
 * It contains a list of IP addresses and ports of game servers matching the
 * requested criteria.
 *
 * @author Sebastian Staudt
 * @see com.github.koraktor.steamcondenser.steam.servers.MasterServer#getServers
 */
public class M2A_SERVER_BATCH_Paket extends SteamPacket
{
    private Vector<String> serverArray;

    /**
     * Creates a new M2A_SERVER_BATCH response object based on the given data
     *
     * @param data The raw packet data replied from the server
     * @throws PacketFormatException if the packet data is not well formatted
     */
    public M2A_SERVER_BATCH_Paket(byte[] data)
    throws PacketFormatException
    {
        super(SteamPacket.M2A_SERVER_BATCH_HEADER, data);

        if(this.contentData.getByte() != 0x0A)
        {
            throw new PacketFormatException("Master query response is missing additional 0x0A byte.");
        }

        int firstOctet, secondOctet, thirdOctet, fourthOctet, portNumber;
        this.serverArray = new Vector<String>();

        do
        {
            firstOctet = this.contentData.getByte() & 0xFF;
            secondOctet = this.contentData.getByte() & 0xFF;
            thirdOctet = this.contentData.getByte() & 0xFF;
            fourthOctet = this.contentData.getByte() & 0xFF;
            portNumber = this.contentData.getShort() & 0xFFFF;

            this.serverArray.add(firstOctet + "." + secondOctet + "." + thirdOctet + "." + fourthOctet + ":" + portNumber);
        }
        while(this.contentData.remaining() > 0);
    }

    /**
     * Returns the list of servers returned from the server in this packet
     *
     * @return An array of server addresses (i.e. IP addresses + port numbers)
     */
    public Vector<String> getServers()
    {
        return this.serverArray;
    }
}
