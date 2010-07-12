/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.packets;

import java.util.Vector;

import com.github.koraktor.steamcondenser.exceptions.PacketFormatException;

/**
 * Represents a response of a master server.
 * @author Sebastian Staudt
 */
public class M2A_SERVER_BATCH_Paket extends SteamPacket
{
    private Vector<String> serverArray;

    public M2A_SERVER_BATCH_Paket(byte[] data)
    throws PacketFormatException
    {
	super(SteamPacket.M2A_SERVER_BATCH_HEADER, data);

	if(this.contentData.getByte() != 0x0A)
	{
	    throw new PacketFormatException("Master query response is missing additional 0x0A byte.");
	}

	int firstOctet, secondOctet, thirdOctet, fourthOctet, portNumber;
	this.serverArray = new Vector<String>();

	do
	{
	    firstOctet = this.contentData.getByte() & 0xFF;
	    secondOctet = this.contentData.getByte() & 0xFF;
	    thirdOctet = this.contentData.getByte() & 0xFF;
	    fourthOctet = this.contentData.getByte() & 0xFF;
	    portNumber = this.contentData.getShort() & 0xFFFF;

	    this.serverArray.add(firstOctet + "." + secondOctet + "." + thirdOctet + "." + fourthOctet + ":" + portNumber);
	}
	while(this.contentData.remaining() > 0);
    }

    public Vector<String> getServers()
    {
	return this.serverArray;
    }
}
