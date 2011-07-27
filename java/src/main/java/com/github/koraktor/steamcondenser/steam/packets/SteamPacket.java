/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

import com.github.koraktor.steamcondenser.PacketBuffer;

/**
 * This module implements the basic functionality used by most of the packets
 * used in communication with master, Source or GoldSrc servers.
 *
 * @author Sebastian Staudt
 * @see SteamPacketFactory
 */
abstract public class SteamPacket
{
    public static final byte A2S_INFO_HEADER = 0x54;
    public static final byte S2A_INFO2_HEADER = 0x49;
    public static final byte S2A_INFO_DETAILED_HEADER = 0x6D;
    public static final byte A2S_PLAYER_HEADER = 0x55;
    public static final byte S2A_PLAYER_HEADER = 0x44;
    public static final byte A2S_RULES_HEADER = 0x56;
    public static final byte S2A_RULES_HEADER = 0x45;
    public static final byte A2S_SERVERQUERY_GETCHALLENGE_HEADER = 0x57;
    public static final byte S2C_CHALLENGE_HEADER = 0x41;
    public static final byte A2M_GET_SERVERS_BATCH2_HEADER = 0x31;
    public static final byte C2M_CHECKMD5_HEADER = 0x4D;
    public static final byte M2A_SERVER_BATCH_HEADER = 0x66;
    public static final byte M2C_ISVALIDMD5_HEADER = 0x4E;
    public static final byte M2S_REQUESTRESTART_HEADER = 0x4F;
    public static final byte RCON_GOLDSRC_CHALLENGE_HEADER = 0x63;
    public static final byte RCON_GOLDSRC_NO_CHALLENGE_HEADER = 0x39;
    public static final byte RCON_GOLDSRC_RESPONSE_HEADER = 0x6c;
    public static final byte S2A_LOGSTRING_HEADER = 0x52;
    public static final byte S2M_HEARTBEAT2_HEADER = 0x30;

    /**
     * This variable stores the content of the package
     */
    protected PacketBuffer contentData;

    /**
     * This byte stores the type of the packet
     */
    protected byte headerData;


    /**
     * Creates a new packet object based on the given data
     *
     * @param headerData The packet header
     */
    protected SteamPacket(byte headerData)
    {
        this(headerData, new byte[0]);
    }

    /**
     * Creates a new packet object based on the given data
     *
     * @param headerData The packet header
     * @param contentBytes The raw data of the packet
     */
    protected SteamPacket(byte headerData, byte[] contentBytes)
    {
        this.contentData = new PacketBuffer(contentBytes);
        this.headerData = headerData;
    }

    /**
     * Returns the raw data representing this packet
     *
     * @return A byte array containing the raw data of this request packet
     */
    public byte[] getBytes()
    {
        byte[] bytes = new byte[this.contentData.getLength() + 5];
        bytes[0] = (byte) 0xFF;
        bytes[1] = (byte) 0xFF;
        bytes[2] = (byte) 0xFF;
        bytes[3] = (byte) 0xFF;
        bytes[4] = this.headerData;
        System.arraycopy(this.contentData.array(), 0, bytes, 5, bytes.length - 5);
        return bytes;
    }
}
