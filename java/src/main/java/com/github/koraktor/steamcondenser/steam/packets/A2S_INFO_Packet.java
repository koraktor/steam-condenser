/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

/**
 * The A2S_INFO_Packet class represents a A2S_INFO request send to the server
 *
 * @author Sebastian Staudt
 * @see com.github.koraktor.steamcondenser.steam.servers.GameServer#updateServerInfo()
 */
public class A2S_INFO_Packet extends SteamPacket {

    /**
     * Creates a new A2S_INFO request object
     */
	public A2S_INFO_Packet() {
		super(SteamPacket.A2S_INFO_HEADER, "Source Engine Query\0".getBytes());
	}
}
