/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

import java.util.HashMap;
import java.util.Map;

import com.github.koraktor.steamcondenser.PacketBuffer;
import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;

/**
 * The S2M_HEARTBEAT2 packet type is used to signal a game servers availability
 * and status to the master servers.
 *
 * @author Sebastian Staudt
 */
public class S2M_HEARTBEAT2_Packet extends SteamPacket
{
    private static final Map<String, Object> DEFAULT_DATA = new HashMap<String, Object>() {{
        put("appid", 320);
        put("bot", 0);
        put("challenge", null);
        put("dedicated", 0);
        put("gamedir", "hl2mp");
        put("gameport", 27015);
        put("gametype", "ctf");
        put("lan", 1);
        put("map", "null");
        put("max", 24);
        put("os", "l");
        put("password", 0);
        put("players", 0);
        put("product", "hl2dm");
        put("protocol", 7);
        put("region", 255);
        put("secure", 0);
        put("specport", 0);
        put("type", "d");
        put("version", "1.0.0.0");
    }};

    /**
     * Creates a new heartbeat packet to send to a master server
     *
     * @param data The server data to send with the heartbeat to the master
     *        server
     * @throws SteamCondenserException when the required challenge number is
     *         missing
     */
    public S2M_HEARTBEAT2_Packet(Map<String, Object> data)
            throws SteamCondenserException {
        super(SteamPacket.S2M_HEARTBEAT2_HEADER);

        HashMap<String, Object> newData = new HashMap<String, Object>();
        newData.putAll(DEFAULT_DATA);
        newData.putAll(data);

        if(newData.get("challenge") == null) {
            throw new SteamCondenserException("You have to provide a challenge number when sending a heartbeat to a master server.");
        }

        String bytes = "\n";
        for(Map.Entry<String, Object> entry : newData.entrySet()) {
            bytes += "\\" + entry.getKey() + "\\" + entry.getValue();
        }
        bytes += "\n";

        this.contentData = new PacketBuffer(bytes.getBytes());
    }

    public byte[] getBytes() {
        byte[] bytes = new byte[1 + this.contentData.array().length];
        bytes[0] = this.headerData;
        System.arraycopy(this.contentData.array(), 0, bytes, 1, this.contentData.array().length);

        return bytes;
    }


}
