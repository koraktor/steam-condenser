/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.CRC32;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import com.github.koraktor.steamcondenser.Helper;
import com.github.koraktor.steamcondenser.exceptions.PacketFormatException;
import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.exceptions.UncompletePacketException;
import com.github.koraktor.steamcondenser.steam.packets.rcon.RCONGoldSrcResponsePacket;

/**
 * @author Sebastian Staudt
 */
public abstract class SteamPacketFactory {
	public static SteamPacket getPacketFromData(byte[] rawData)
			throws PacketFormatException {
		byte header = rawData[0];
		byte[] data = new byte[rawData.length - 1];
		System.arraycopy(rawData, 1, data, 0, rawData.length - 1);

		switch (header) {
		case SteamPacket.A2S_INFO_HEADER:
			return new A2S_INFO_Packet();

		case SteamPacket.S2A_INFO_DETAILED_HEADER:
			return new S2A_INFO_DETAILED_Packet(data);

		case SteamPacket.S2A_INFO2_HEADER:
			return new S2A_INFO2_Packet(data);

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

        case SteamPacket.M2C_ISVALIDMD5_HEADER:
            return new M2C_ISVALIDMD5_Packet(data);

        case SteamPacket.M2S_REQUESTRESTART_HEADER:
            return new M2S_REQUESTRESTART_Packet(data);

		case SteamPacket.RCON_GOLDSRC_CHALLENGE_HEADER:
		case SteamPacket.RCON_GOLDSRC_NO_CHALLENGE_HEADER:
		case SteamPacket.RCON_GOLDSRC_RESPONSE_HEADER:
			return new RCONGoldSrcResponsePacket(data);

        case SteamPacket.S2A_LOGSTRING_HEADER:
            return new S2A_LOGSTRING_Packet(data);

		default:
			throw new PacketFormatException("Unknown packet with header 0x"
					+ Integer.toHexString(header) + " received.");
		}
	}

	public static SteamPacket reassemblePacket(ArrayList<byte[]> splitPackets)
			throws IOException, SteamCondenserException {
		return SteamPacketFactory.reassemblePacket(splitPackets, false, 0, 0);
	}

	public static SteamPacket reassemblePacket(ArrayList<byte[]> splitPackets,
			boolean isCompressed, int uncompressedSize, int packetChecksum)
			throws IOException, SteamCondenserException {
		byte[] packetData, tmpData;
		packetData = new byte[0];

		for (byte[] splitPacket : splitPackets) {
			if (splitPacket == null) {
				throw new UncompletePacketException();
			}
			tmpData = packetData;
			packetData = new byte[tmpData.length + splitPacket.length];
			System.arraycopy(tmpData, 0, packetData, 0, tmpData.length);
			System.arraycopy(splitPacket, 0, packetData, tmpData.length,
					splitPacket.length);
		}

		if (isCompressed) {
			ByteArrayInputStream stream = new ByteArrayInputStream(packetData);
			stream.read();
			stream.read();
			BZip2CompressorInputStream bzip2 = new BZip2CompressorInputStream(stream);
			byte[] uncompressedPacketData = new byte[uncompressedSize];
			bzip2.read(uncompressedPacketData, 0, uncompressedSize);

			CRC32 crc32 = new CRC32();
			crc32.update(uncompressedPacketData);
			int crc32checksum = (int) crc32.getValue();

			if (crc32checksum != packetChecksum) {
				throw new PacketFormatException(
						"CRC32 checksum mismatch of uncompressed packet data.");
			}
			packetData = uncompressedPacketData;
		}

		packetData = new String(packetData).substring(4).getBytes();

		return SteamPacketFactory.getPacketFromData(packetData);
	}
}
