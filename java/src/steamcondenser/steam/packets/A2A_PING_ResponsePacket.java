/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.packets;

import steamcondenser.PacketFormatException;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_PING_ResponsePacket extends SteamPacket
{
	public A2A_PING_ResponsePacket(byte[] dataBytes)
		throws PacketFormatException
	{
		super(SteamPacket.A2A_PING_RESPONSE_HEADER, dataBytes);
		
		String pingReply = this.contentData.getString();
		
		if(!pingReply.equals("") && !pingReply.equals("00000000000000"))
		{
			throw new PacketFormatException("Wrong formatted A2A_PING Response Packet.");
		}
	}
}
