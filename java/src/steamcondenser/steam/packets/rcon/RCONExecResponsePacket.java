/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.packets.rcon;

public class RCONExecResponsePacket extends RCONPacket
{
    public RCONExecResponsePacket(int requestId, String commandReturn)
    {
	super(requestId, RCONPacket.SERVERDATA_RESPONSE_VALUE, commandReturn);
    }

    public String getResponse()
    {
	return new String(this.contentData.array());
    }
}
