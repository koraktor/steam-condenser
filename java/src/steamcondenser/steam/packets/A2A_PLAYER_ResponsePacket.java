package steamcondenser.steam.packets;

import java.util.ArrayList;

import steamcondenser.PacketFormatException;
import steamcondenser.steam.SteamPlayer;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class A2A_PLAYER_ResponsePacket extends SteamPacket
{
	private ArrayList<SteamPlayer> playerArray;

	public A2A_PLAYER_ResponsePacket(byte[] dataBytes)
		throws PacketFormatException
	{
		super(SteamPacket.A2A_PLAYER_RESPONSE_HEADER, dataBytes);
		
		if(this.contentData.getLength() == 0)
		{
			throw new PacketFormatException("Wrong formatted A2A_PLAYER response packet.");
		}
		
		this.playerArray = new ArrayList<SteamPlayer>(this.contentData.getByte());
		
		while(this.contentData.hasRemaining())
		{
			this.playerArray.add(new SteamPlayer(
					this.contentData.getByte(),
					this.contentData.getString(),
					Integer.reverseBytes(this.contentData.getInt()),
					Float.intBitsToFloat(Integer.reverseBytes(this.contentData.getInt()))
			));
		}
	}
	
	public ArrayList<SteamPlayer> getPlayerArray()
	{
		return this.playerArray;
	}
}
