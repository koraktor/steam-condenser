/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

import java.util.HashMap;

/**
 * This class represents a S2A_INFO_DETAILED response packet sent by a GoldSrc
 * server
 *
 * This is deprecated by 10/24/2008. GoldSrc servers use the same format as
 * Source servers now (see {@link S2A_INFO2_Packet}).
 *
 * @author Sebastian Staudt
 * @see com.github.koraktor.steamcondenser.steam.servers.GameServer#updateServerInfo
 */
public class S2A_INFO_DETAILED_Packet extends S2A_INFO_BasePacket {
	protected boolean isMod;
	protected HashMap<String, Object> modInfo;
	protected String serverIp;

    /**
     * Creates a new S2A_INFO_DETAILED response object based on the given data
     *
     * @param dataBytes The raw packet data replied from the server
     */
	public S2A_INFO_DETAILED_Packet(byte[] dataBytes) {
		super(SteamPacket.S2A_INFO_DETAILED_HEADER, dataBytes);

		this.serverIp = this.contentData.getString();
		this.serverName = this.contentData.getString();
		this.mapName = this.contentData.getString();
		this.gameDir = this.contentData.getString();
		this.gameDescription = this.contentData.getString();
		this.numberOfPlayers = this.contentData.getByte();
		this.maxPlayers = this.contentData.getByte();
		this.networkVersion = this.contentData.getByte();
		this.dedicated = this.contentData.getByte();
		this.operatingSystem = this.contentData.getByte();
		this.passwordProtected = this.contentData.getByte() == 1;
		this.isMod = this.contentData.getByte() == 1;

		if (this.isMod) {
			this.modInfo = new HashMap<String, Object>(6);
			this.modInfo.put("urlInfo", this.contentData.getString());
			this.modInfo.put("urlDl", this.contentData.getString());
			this.contentData.getByte();
			if (this.contentData.remaining() == 12) {
				this.modInfo.put("modVersion", Integer.reverseBytes(this.contentData.getInt()));
				this.modInfo.put("modSize", Integer.reverseBytes(this.contentData.getInt()));
				this.modInfo.put("svOnly", this.contentData.getByte() == 1);
				this.modInfo.put("clDll", this.contentData.getByte() == 1);
				this.secure = this.contentData.getByte() == 1;
				this.numberOfBots = this.contentData.getByte();
			}
		} else {
			this.secure = this.contentData.getByte() == 1;
			this.numberOfBots = this.contentData.getByte();
		}
	}
}
