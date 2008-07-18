package steamcondenser.steam.packets;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_PING_RequestPacket extends SteamPacket
{
	public A2A_PING_RequestPacket()
	{
		super(null);
	}
	
	public byte[] getBytes()
	{
		byte[] splitPacketHeader = Long.toBinaryString(-1).getBytes();
		byte packetHeader = 0x69;
		
		byte[] returnValue = new byte[splitPacketHeader.length + 1];
		System.arraycopy(splitPacketHeader, 0, returnValue, 0, splitPacketHeader.length);
		System.arraycopy(packetHeader, 0, returnValue, splitPacketHeader.length, 1);
		
		return returnValue;
	}
}
