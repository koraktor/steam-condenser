package steamcondenser.steam.packets;

public class RequestPacketWithChallenge extends SteamPacket
{
	public RequestPacketWithChallenge(byte headerByte, long challengeNumber)
	{
		super(headerByte, Long.toHexString(challengeNumber).getBytes());
	}
}
