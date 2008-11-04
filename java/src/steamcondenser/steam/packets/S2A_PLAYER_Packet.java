/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.packets;

import java.util.ArrayList;

import steamcondenser.PacketFormatException;
import steamcondenser.steam.SteamPlayer;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class S2A_PLAYER_Packet extends SteamPacket
{
    private ArrayList<SteamPlayer> playerArray;

    public S2A_PLAYER_Packet(byte[] dataBytes)
    throws PacketFormatException
    {
	super(SteamPacket.S2A_PLAYER_HEADER, dataBytes);

	if(this.contentData.getLength() == 0)
	{
	    throw new PacketFormatException("Wrong formatted A2A_PLAYER response packet.");
	}

	this.playerArray = new ArrayList<SteamPlayer>(this.contentData.getByte());

	while(this.contentData.hasRemaining())
	{
	    this.playerArray.add(new SteamPlayer(
		    this.contentData.getByte(),
		    this.contentData.getString(),
		    Integer.reverseBytes(this.contentData.getInt()),
		    Float.intBitsToFloat(Integer.reverseBytes(this.contentData.getInt()))
	    ));
	}
    }

    public ArrayList<SteamPlayer> getPlayerArray()
    {
	return this.playerArray;
    }
}
