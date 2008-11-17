package steamcondenser.steam.packets.rcon;

import steamcondenser.PacketBuffer;
import steamcondenser.PacketFormatException;
import steamcondenser.steam.packets.SteamPacketFactory;

public abstract class RCONPacketFactory extends SteamPacketFactory
{
    public static RCONPacket getPacketFromData(byte[] rawData) 
    	throws PacketFormatException
    {
	PacketBuffer packetBuffer = new PacketBuffer(rawData);

	int packetSize = Integer.reverseBytes(packetBuffer.getInt());
	int requestId = Integer.reverseBytes(packetBuffer.getInt());
	int header = Integer.reverseBytes(packetBuffer.getInt());
	String data = packetBuffer.getString();

	switch(header)
	{
	case RCONPacket.SERVERDATA_AUTH_RESPONSE:
	    return new RCONAuthResponse(requestId);
	case RCONPacket.SERVERDATA_RESPONSE_VALUE:
	    return new RCONExecResponsePacket(requestId, data);
	default:
	    throw new PacketFormatException("Unknown packet with header " + Integer.reverseBytes(header) + " received.");
	}
    }
}
