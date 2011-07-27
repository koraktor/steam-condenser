/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

import java.util.HashMap;
import com.github.koraktor.steamcondenser.exceptions.PacketFormatException;
import com.github.koraktor.steamcondenser.steam.SteamPlayer;

/**
 * This class represents a S2A_PLAYER response sent by a game server
 * <p>
 * It is used to transfer a list of players currently playing on the server.
 *
 * @author Sebastian Staudt
 * @see com.github.koraktor.steamcondenser.steam.servers.GameServer#updatePlayerInfo
 */
public class S2A_PLAYER_Packet extends SteamPacket {

    private HashMap<String, SteamPlayer> playerHash;

    /**
     * Creates a new S2A_PLAYER response object based on the given data
     *
     * @param dataBytes The raw packet data sent by the server
     */
    public S2A_PLAYER_Packet(byte[] dataBytes)
            throws PacketFormatException {
        super(SteamPacket.S2A_PLAYER_HEADER, dataBytes);

        if(this.contentData.getLength() == 0) {
            throw new PacketFormatException("Wrong formatted S2A_PLAYER response packet.");
        }

        this.playerHash = new HashMap<String, SteamPlayer>(this.contentData.getByte());

        while(this.contentData.hasRemaining()) {
            byte playerId = this.contentData.getByte();
            String playerName = this.contentData.getString();
            this.playerHash.put(playerName, new SteamPlayer(
                playerId,
                playerName,
                Integer.reverseBytes(this.contentData.getInt()),
                Float.intBitsToFloat(Integer.reverseBytes(this.contentData.getInt()))
            ));
        }
    }

    /**
     * Returns the list of active players provided by the server
     *
     * @return All active players on the server
     */
    public HashMap<String, SteamPlayer> getPlayerHash() {
        return this.playerHash;
    }
}
