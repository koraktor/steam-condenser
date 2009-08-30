/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */
package steamcondenser.steam.packets;

import java.util.HashMap;

import steamcondenser.PacketFormatException;

/**
 * @author Sebastian Staudt
 */
public class S2A_RULES_Packet extends SteamPacket
{
    private HashMap<String, String> rulesHash;

    public S2A_RULES_Packet(byte[] dataBytes)
            throws PacketFormatException {
        super(SteamPacket.S2A_RULES_HEADER, dataBytes);

        if (this.contentData.getLength() == 0) {
            throw new PacketFormatException("Wrong formatted A2A_RULES response packet.");
        }

        int rulesCount = Short.reverseBytes(this.contentData.getShort());
        this.rulesHash = new HashMap<String, String>(rulesCount);
        for (int i = 0; i < rulesCount; i++) {
            this.rulesHash.put(this.contentData.getString(), this.contentData.getString());
        }
    }

    public HashMap<String, String> getRulesHash() {
        return this.rulesHash;
    }
}
