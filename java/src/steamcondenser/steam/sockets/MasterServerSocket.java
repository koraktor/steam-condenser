package steamcondenser.steam.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeoutException;

import steamcondenser.PacketFormatException;
import steamcondenser.SteamCondenserException;
import steamcondenser.steam.packets.SteamPacket;

public class MasterServerSocket extends SteamSocket
{
	public MasterServerSocket(InetAddress ipAddress, int portNumber)
		throws IOException
	{
		super(ipAddress, portNumber);
	}

	public SteamPacket getReply()
		throws IOException, TimeoutException, SteamCondenserException
	{
		this.receivePacket(1500);
		
		if(this.buffer.getInt() != -1)
		{
			throw new PacketFormatException("Master query response has wrong packet header.");
		}
		
		return this.createPacket();
	}

}
