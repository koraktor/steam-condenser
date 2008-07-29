package steamcondenser.steam;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

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
	
	public SteamPacket receive()
		throws IOException
	{
		byte[] buffer = new byte[1400];
		DatagramPacket replyPacket = new DatagramPacket(buffer, 1400);
		super.receive(replyPacket);
		
		byte[] packetData = replyPacket.getData();
		byte headerData = packetData[0];
		byte[] contentData = new byte[packetData.length - 1];
		System.arraycopy(packetData, 1, contentData, 0, packetData.length - 1);
		
		return new SteamPacket(headerData, contentData);
	}
	
	/**
	 * @param dataPacket The {@link steamcondenser.steam.packets.SteamPacket SteamPacket} to send to the remote end
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