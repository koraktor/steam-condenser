package steamcondenser.steam.packets.rcon;

import steamcondenser.steam.packets.SteamPacket;

public class RCONGoldSrcResponsePacket extends SteamPacket
{
	public RCONGoldSrcResponsePacket(byte[] commandResponse)
	{
		super(SteamPacket.RCON_GOLDSRC_RESPONSE_HEADER, commandResponse);
	}
	
	public String getResponse()
	{
		return this.contentData.getString();
	}
}
