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
	
	public A2A_RULES_ResponsePacket(byte[] contentData)
		throws Exception
	{
		super(SteamPacket.A2A_RULES_RESPONSE_HEADER, contentData);
		
		if(contentData.length == 0)
		{
			throw new Exception("Wrong formatted A2A_RULES response packet.");
		}
		
		PacketBuffer contentBuffer = new PacketBuffer(contentData);
		
		int numberOfRules = contentBuffer.getByte();
		while(contentBuffer.hasRemaining())
		{
			this.rulesHash.put(contentBuffer.getString(), contentBuffer.getString());
		}
	}
}
