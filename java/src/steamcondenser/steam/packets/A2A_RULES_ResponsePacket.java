package steamcondenser.steam.packets;

import java.util.HashMap;

import steamcondenser.PacketBuffer;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_RULES_ResponsePacket extends SteamPacket
{
	private HashMap<String, String>rulesHash;
	
	public A2A_RULES_ResponsePacket(byte[] dataBytes)
		throws Exception
	{
		super(SteamPacket.A2A_RULES_RESPONSE_HEADER, dataBytes);
		
		if(this.contentData.getLength() == 0)
		{
			throw new Exception("Wrong formatted A2A_RULES response packet.");
		}
		
		int numberOfRules = this.contentData.getByte();
		while(this.contentData.hasRemaining())
		{
			this.rulesHash.put(this.contentData.getString(), this.contentData.getString());
		}
	}
}
