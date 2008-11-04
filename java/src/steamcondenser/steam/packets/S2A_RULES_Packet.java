/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.packets;

import java.util.HashMap;

import steamcondenser.PacketFormatException;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class S2A_RULES_Packet extends SteamPacket
{
    private HashMap<String, String>rulesHash;

    public S2A_RULES_Packet(byte[] dataBytes)
    throws PacketFormatException
    {
	super(SteamPacket.S2A_RULES_HEADER, dataBytes);

	if(this.contentData.getLength() == 0)
	{
	    throw new PacketFormatException("Wrong formatted A2A_RULES response packet.");
	}

	this.rulesHash = new HashMap<String, String>(Short.reverseBytes(this.contentData.getShort()));

	while(this.contentData.hasRemaining())
	{
	    this.rulesHash.put(this.contentData.getString(), this.contentData.getString());
	}
    }

    public HashMap<String, String> getRulesHash()
    {
	return this.rulesHash;
    }
}
