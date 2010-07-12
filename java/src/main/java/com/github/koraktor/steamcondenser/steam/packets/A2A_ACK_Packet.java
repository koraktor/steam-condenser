/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package com.github.koraktor.steamcondenser.steam.packets;

import com.github.koraktor.steamcondenser.exceptions.PacketFormatException;

/**
 * @author Sebastian Staudt
 */
public class A2A_ACK_Packet extends SteamPacket
{
    public A2A_ACK_Packet(byte[] dataBytes)
    throws PacketFormatException
    {
	super(SteamPacket.A2A_ACK_HEADER, dataBytes);

	String pingReply = this.contentData.getString();

	if(!pingReply.equals("") && !pingReply.equals("00000000000000"))
	{
	    throw new PacketFormatException("Wrong formatted A2A_PING Response Packet.");
	}
    }
}
