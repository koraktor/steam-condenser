package steamcondenser.steam.packets;

import steamcondenser.steam.SteamPlayer;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_PLAYER_ResponsePacket extends SteamPacket
{
	private SteamPlayer[] playerArray;

	public A2A_PLAYER_ResponsePacket(byte[] contentData)
		throws Exception
	{
		super(SteamPacket.A2A_PLAYER_RESPONSE_HEADER, contentData);
		
		if(contentData.length == 0)
		{
			throw new Exception("Wrong formatted A2A_PLAYER response packet.");
		}
		
		int number_of_players = Byte.valueOf(this.contentData[0]).intValue();
		
		// TODO
	}
	
	public SteamPlayer[] getPlayerArray()
	{
		return this.playerArray;
	}
}
