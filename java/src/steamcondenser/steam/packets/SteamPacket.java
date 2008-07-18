package steamcondenser.steam.packets;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class SteamPacket
{
	private byte[] data;
	
	public SteamPacket(byte[] data)
	{
		this.data = data;
	}
	
	public byte[] getBytes()
	{
		return this.data;
	}
}
