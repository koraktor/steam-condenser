/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

/**
 * This class represents a S2A_INFO_DETAILED response packet sent by a Source
 * or GoldSrc server
 * <p>
 * Out-of-date (before 10/24/2008) GoldSrc servers use an older format (see
 * {@link S2A_INFO_DETAILED_Packet}).
 *
 * @author Sebastian Staudt
 * @see com.github.koraktor.steamcondenser.steam.servers.GameServer#updateServerInfo
 */
public class S2A_INFO2_Packet extends S2A_INFO_BasePacket
{
    protected short appId;
    protected String gameVersion;
    protected long serverId;
    protected short serverPort;
    protected String serverTags;
    protected String tvName;
    protected short tvPort;

    /**
     * Creates a new S2A_INFO2 response object based on the given data
     *
     * @param dataBytes The raw packet data replied from the server
     */
    public S2A_INFO2_Packet(byte[] dataBytes)
    {
	super(SteamPacket.S2A_INFO2_HEADER, dataBytes);

	this.networkVersion = this.contentData.getByte();
	this.serverName = this.contentData.getString();
	this.mapName = this.contentData.getString();
	this.gameDir = this.contentData.getString();
	this.gameDescription = this.contentData.getString();
	this.appId = Short.reverseBytes(this.contentData.getShort());
	this.numberOfPlayers = this.contentData.getByte();
	this.maxPlayers = this.contentData.getByte();
	this.numberOfBots = this.contentData.getByte();
	this.dedicated = this.contentData.getByte();
	this.operatingSystem = this.contentData.getByte();
	this.passwordProtected = this.contentData.getByte() == 1;
	this.secure = this.contentData.getByte() == 1;
	this.gameVersion = this.contentData.getString();

	if(this.contentData.remaining() > 0)
	{
	    byte extraDataFlag = this.contentData.getByte();

	    if((extraDataFlag & 0x80) != 0)
	    {
		this.serverPort = Short.reverseBytes(this.contentData.getShort());
	    }

        if((extraDataFlag & 0x10) != 0) {
            this.serverId = Long.reverseBytes((this.contentData.getInt() << 32) | this.contentData.getInt());
        }

	    if((extraDataFlag & 0x40) != 0)
	    {
		this.tvPort = Short.reverseBytes(this.contentData.getShort());
		this.tvName = this.contentData.getString();
	    }

	    if((extraDataFlag & 0x20) != 0)
	    {
		this.serverTags = this.contentData.getString();
	    }
	}
    }
}
