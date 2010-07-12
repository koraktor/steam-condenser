/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeoutException;

import com.github.koraktor.steamcondenser.exceptions.PacketFormatException;
import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.packets.SteamPacket;

/**
 * A socket used for connections to Steam master servers.
 * @author Sebastian Staudt
 */
public class MasterServerSocket extends QuerySocket
{
    public MasterServerSocket(InetAddress ipAddress, int portNumber)
    throws IOException
    {
	super(ipAddress, portNumber);
    }

    public SteamPacket getReply()
    throws IOException, TimeoutException, SteamCondenserException
    {
	this.receivePacket(1500);

	if(this.buffer.getInt() != -1)
	{
	    throw new PacketFormatException("Master query response has wrong packet header.");
	}

	return this.getPacketFromData();
    }

}
