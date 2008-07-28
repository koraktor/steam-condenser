package steamcondenser.steam.packets;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_PING_ResponsePacket extends SteamPacket
{
	public A2A_PING_ResponsePacket(byte[] contentData)
		throws Exception
	{
		super(SteamPacket.A2A_PING_RESPONSE_HEADER, contentData);
		
		if(contentData.toString() != "00000000000000\0")
		{
			throw new Exception("Wrong formatted A2A_PING Response Packet.");
		}
	}
}
