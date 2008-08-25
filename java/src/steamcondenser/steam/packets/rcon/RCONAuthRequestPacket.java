/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.packets.rcon;

public class RCONAuthRequestPacket extends RCONPacket
{
	public RCONAuthRequestPacket(int requestId, String rconPassword)
	{
		super(requestId, RCONPacket.SERVERDATA_AUTH, rconPassword);
	}
}
