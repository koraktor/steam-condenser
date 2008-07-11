package steamcondenser.steam;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class SteamSocket extends DatagramSocket
{
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
