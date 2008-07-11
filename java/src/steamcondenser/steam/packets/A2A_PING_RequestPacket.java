package steamcondenser.steam.packets;

import steamcondenser.steam.SteamPacket;

public class A2A_PING_RequestPacket extends SteamPacket
{
	public byte[] getBytes()
	{
		long splitPacketHeader = -1;
		byte packetHeader = 0x69;
		
		return Long.toBinaryString(splitPacketHeader).getBytes() + packetHeader;
	}
}
