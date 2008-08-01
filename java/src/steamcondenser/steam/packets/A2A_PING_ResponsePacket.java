package steamcondenser.steam.packets;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_PING_ResponsePacket extends SteamPacket
{
	public A2A_PING_ResponsePacket(byte[] dataBytes)
		throws Exception
	{
		super(SteamPacket.A2A_PING_RESPONSE_HEADER, dataBytes);
		
		String pingReply = this.contentData.getString();
		
		if(!pingReply.equals("") && !pingReply.equals("00000000000000"))
		{
			throw new Exception("Wrong formatted A2A_PING Response Packet.");
		}
	}
}
