package steamcondenser.steam;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Logger;

import steamcondenser.steam.packets.SteamPacket;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class SteamSocket extends DatagramSocket
{
	/**
	 * @param ipAddress The IP of the server to connect to
	 * @param portNumber The port number of the server
	 */
	public SteamSocket(InetAddress ipAddress, int portNumber)
		throws SocketException
	{
		super();
		this.connect(ipAddress, portNumber);
	}
	
	public SteamPacket getReply()
	  throws IOException, Exception
	{
		SteamPacket replyPacket = this.receive();

		Logger.getLogger("global").info("Sending data packet of type \"" + replyPacket.getClass().getSimpleName() + "\"");

		return replyPacket;
	}
	
	private SteamPacket receive()
		throws IOException, Exception
	{
		byte[] buffer = new byte[1400];
		DatagramPacket replyPacket = new DatagramPacket(buffer, 1400);
		super.receive(replyPacket);
		
		return SteamPacket.createPacket(replyPacket.getData());
	}
	
	/**
	 * @param dataPacket The {@link steamcondenser.steam.packets.SteamPacket SteamPacket} to send to the remote end
	 */
	public void send(SteamPacket dataPacket)
		throws IOException
	{
		Logger.getLogger("global").info("Sending data packet of type \"" + dataPacket.getClass().getSimpleName() + "\"");

		byte[] data = dataPacket.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(data, data.length, this.getRemoteSocketAddress());
		super.send(sendPacket);
	}
	
	public void finalize()
	{
		this.close();
	}
}
