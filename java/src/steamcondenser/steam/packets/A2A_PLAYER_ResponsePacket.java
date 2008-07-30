package steamcondenser.steam.packets;

import java.util.ArrayList;

import steamcondenser.steam.SteamPlayer;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_PLAYER_ResponsePacket extends SteamPacket
{
	private ArrayList<SteamPlayer> playerArray;

	public A2A_PLAYER_ResponsePacket(byte[] dataBytes)
		throws Exception
	{
		super(SteamPacket.A2A_PLAYER_RESPONSE_HEADER, dataBytes);
		
		if(this.contentData.getLength() == 0)
		{
			throw new Exception("Wrong formatted A2A_PLAYER response packet.");
		}
		
		int numberOfPlayers = this.contentData.getByte();
		
		this.playerArray = new ArrayList<SteamPlayer>(numberOfPlayers);
		
		while(this.contentData.hasRemaining())
		{
			this.playerArray.add(new SteamPlayer(this.contentData.getByte(), this.contentData.getString(), Integer.reverseBytes(this.contentData.getInt()), this.contentData.getFloat()));
		}
	}
	
	public ArrayList<SteamPlayer> getPlayerArray()
	{
		return this.playerArray;
	}
}
