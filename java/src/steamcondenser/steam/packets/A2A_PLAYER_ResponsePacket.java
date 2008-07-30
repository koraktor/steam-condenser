package steamcondenser.steam.packets;

import steamcondenser.steam.SteamPlayer;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_PLAYER_ResponsePacket extends SteamPacket
{
	private SteamPlayer[] playerArray;

	public A2A_PLAYER_ResponsePacket(byte[] dataBytes)
		throws Exception
	{
		super(SteamPacket.A2A_PLAYER_RESPONSE_HEADER, dataBytes);
		
		if(this.contentData.getLength() == 0)
		{
			throw new Exception("Wrong formatted A2A_PLAYER response packet.");
		}
		
		int number_of_players = this.contentData.getByte();
		
		// TODO
	}
	
	public SteamPlayer[] getPlayerArray()
	{
		return this.playerArray;
	}
}
