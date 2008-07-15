package steamcondenser.steam;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

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
	
	public void receive()
		throws IOException
	{
		byte[] buffer = new byte[1400];
		DatagramPacket replyPacket = new DatagramPacket(buffer, 1400);
		super.receive(replyPacket);
		System.out.println(replyPacket.getData());
	}
	
	/**
	 * @param dataPacket The {@link steamcondenser.SteamPacket SteamPacket} to send to the remote end
	 */
	public void send(SteamPacket dataPacket)
		throws IOException
	{
		byte[] data = dataPacket.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(data, data.length, this.getRemoteSocketAddress());
		super.send(sendPacket);
	}
	
	public void finalize()
	{
		this.close();
	}
}
