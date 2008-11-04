/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.packets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.zip.CRC32;

import org.apache.tools.bzip2.CBZip2InputStream;

import steamcondenser.Helper;
import steamcondenser.PacketBuffer;
import steamcondenser.PacketFormatException;
import steamcondenser.SteamCondenserException;
import steamcondenser.UncompletePacketException;
import steamcondenser.steam.packets.rcon.RCONGoldSrcResponsePacket;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
abstract public class SteamPacket
{
    public static final byte A2S_INFO_HEADER = 0x54;
    public static final byte S2A_INFO2_HEADER = 0x49;
    public static final byte S2A_INFO_DETAILED_HEADER = 0x6D;
    public static final byte A2A_PING_HEADER = 0x69;
    public static final byte A2A_ACK_HEADER = 0x6A;
    public static final byte A2S_PLAYER_HEADER = 0x55;
    public static final byte S2A_PLAYER_HEADER = 0x44;
    public static final byte A2S_RULES_HEADER = 0x56;
    public static final byte S2A_RULES_HEADER = 0x45;
    public static final byte A2S_SERVERQUERY_GETCHALLENGE_HEADER = 0x57;
    public static final byte S2C_CHALLENGE_HEADER = 0x41;
    public static final byte A2M_GET_SERVERS_BATCH2_HEADER = 0x31;
    public static final byte M2A_SERVER_BATCH_HEADER = 0x66;
    public static final byte RCON_GOLDSRC_NO_CHALLENGE_HEADER = 0x39;
    public static final byte RCON_GOLDSRC_RESPONSE_HEADER = 0x6c;

    protected PacketBuffer contentData;
    protected byte headerData;

    public static SteamPacket createPacket(byte[] rawData)
    throws PacketFormatException
    {
	byte header = rawData[0];
	byte[] data = new byte[rawData.length - 1];
	System.arraycopy(rawData, 1, data, 0, rawData.length - 1);

	switch(header)
	{
	case SteamPacket.A2S_INFO_HEADER:
	    return new A2S_INFO_Packet();

	case SteamPacket.S2A_INFO_DETAILED_HEADER:
	    return new S2A_INFO_DETAILED_Packet(data);

	case SteamPacket.S2A_INFO2_HEADER:
	    return new S2A_INFO2_Packet(data);

	case SteamPacket.A2A_PING_HEADER:
	    return new A2A_PING_Packet();

	case SteamPacket.A2A_ACK_HEADER:
	    return new A2A_ACK_Packet(data);

	case SteamPacket.A2S_PLAYER_HEADER:
	    return new A2S_PLAYER_Packet(Helper.integerFromByteArray(data));

	case SteamPacket.S2A_PLAYER_HEADER:
	    return new S2A_PLAYER_Packet(data);

	case SteamPacket.A2S_RULES_HEADER:
	    return new A2S_RULES_Packet(Helper.integerFromByteArray(data));

	case SteamPacket.S2A_RULES_HEADER:
	    return new S2A_RULES_Packet(data);

	case SteamPacket.A2S_SERVERQUERY_GETCHALLENGE_HEADER:
	    return new A2S_SERVERQUERY_GETCHALLENGE_Packet();

	case SteamPacket.S2C_CHALLENGE_HEADER:
	    return new S2C_CHALLENGE_Packet(data);

	case SteamPacket.M2A_SERVER_BATCH_HEADER:
	    return new M2A_SERVER_BATCH_Paket(data);

	case SteamPacket.RCON_GOLDSRC_RESPONSE_HEADER:
	    return new RCONGoldSrcResponsePacket(data);

	default:
	    throw new PacketFormatException("Unknown packet with header 0x" + Integer.toHexString(header) + " received.");
	}
    }

    public static SteamPacket reassemblePacket(Vector<byte[]> splitPackets)
    throws IOException, SteamCondenserException
    {
	return SteamPacket.reassemblePacket(splitPackets, false, (short) 0, 0);
    }

    public static SteamPacket reassemblePacket(Vector<byte[]> splitPackets, boolean isCompressed, short uncompressedSize, int packetChecksum)
    throws IOException, SteamCondenserException
    {
	byte[] packetData, tmpData;
	packetData = new byte[0];

	for(byte[] splitPacket : splitPackets)
	{
	    if(splitPacket == null)
	    {
		throw new UncompletePacketException();
	    }
	    tmpData = packetData;
	    packetData = new byte[tmpData.length + splitPacket.length];
	    System.arraycopy(tmpData, 0, packetData, 0, tmpData.length);
	    System.arraycopy(splitPacket, 0, packetData, tmpData.length, splitPacket.length);
	}

	if(isCompressed)
	{
	    CBZip2InputStream bzip2 = new CBZip2InputStream(new ByteArrayInputStream(packetData));
	    bzip2.read(packetData, 0, uncompressedSize);

	    CRC32 crc32 = new CRC32();
	    crc32.update(packetData);

	    if(crc32.getValue() != packetChecksum)
	    {
		throw new PacketFormatException("CRC32 checksum mismatch of uncompressed packet data.");
	    }
	}

	return SteamPacket.createPacket(packetData);
    }

    protected SteamPacket(byte headerData)
    {
	this(headerData, new byte[0]);
    }

    protected SteamPacket(byte headerData, byte[] contentBytes)
    {
	this.contentData = new PacketBuffer(contentBytes);
	this.headerData = headerData;
    }

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
