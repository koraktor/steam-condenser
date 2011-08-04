/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

import java.util.HashMap;

import com.github.koraktor.steamcondenser.exceptions.PacketFormatException;

/**
 * This class represents a S2A_RULES response sent by a game server
 * <p>
 * It is used to transfer a list of server rules (a.k.a. CVARs) with their
 * active values.
 *
 * @author Sebastian Staudt
 * @see com.github.koraktor.steamcondenser.steam.servers.GameServer#updateRules
 */
public class S2A_RULES_Packet extends SteamPacket {

    private HashMap<String, String> rulesHash;

    /**
     * Creates a new S2A_RULES response object based on the given data
     *
     * @param dataBytes The raw packet data sent by the server
     */
    public S2A_RULES_Packet(byte[] dataBytes)
            throws PacketFormatException {
        super(SteamPacket.S2A_RULES_HEADER, dataBytes);

        if (this.contentData.getLength() == 0) {
            throw new PacketFormatException("Wrong formatted S2A_RULES response packet.");
        }

        int rulesCount = Short.reverseBytes(this.contentData.getShort());
        this.rulesHash = new HashMap<String, String>(rulesCount);

        String rule;
        String value;
        for (int i = 0; i < rulesCount; i++) {
            rule = this.contentData.getString();
            value = this.contentData.getString();

            // This is a workaround for servers sending corrupt replies
            if(rule.equals("") || value.equals("")) {
                break;
            }

            this.rulesHash.put(rule, value);
        }
    }

    /**
     * Returns the list of server rules (a.k.a. CVars) with the current values
     *
     * @return array A list of server rules
     */
    public HashMap<String, String> getRulesHash() {
        return this.rulesHash;
    }
}
