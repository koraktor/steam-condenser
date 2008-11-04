package steamcondenser.steam.packets.rcon;

import steamcondenser.Helper;
import steamcondenser.steam.packets.SteamPacket;

public class RCONGoldSrcRequestPacket extends SteamPacket
{
    public RCONGoldSrcRequestPacket(String request)
    {
	super((byte) 0, request.getBytes());
    }

    public byte[] getBytes()
    {
	byte[] bytes = new byte[this.contentData.getLength() + 4];

	System.arraycopy(Helper.byteArrayFromInteger(0xFFFFFFFF), 0, bytes, 0, 4);
	System.arraycopy(this.contentData.array(), 0, bytes, 4, this.contentData.getLength());

	return bytes;
    }
}
