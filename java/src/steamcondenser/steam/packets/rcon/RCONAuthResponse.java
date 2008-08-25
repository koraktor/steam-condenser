/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.packets.rcon;

public class RCONAuthResponse extends RCONPacket
{
	public RCONAuthResponse(int requestId)
	{
		super(requestId, RCONPacket.SERVERDATA_AUTH_RESPONSE, "");
	}
}
