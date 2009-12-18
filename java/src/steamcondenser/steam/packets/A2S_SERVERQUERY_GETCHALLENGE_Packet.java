/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.packets;

/**
 * @author Sebastian Staudt
 */
public class A2S_SERVERQUERY_GETCHALLENGE_Packet extends SteamPacket
{
    public A2S_SERVERQUERY_GETCHALLENGE_Packet()
    {
	super(SteamPacket.A2S_SERVERQUERY_GETCHALLENGE_HEADER);
    }
}
