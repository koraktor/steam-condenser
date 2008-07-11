package steamcondenser.steam;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import sip.steam.packets.A2A_PING_RequestPacket;

public class SourceServer
{
	private InetAddress ipAddress;
	
	private int portNumber;
	
	private SteamSocket socket;
	
	public SourceServer(InetAddress ipAddress, int portNumber)
		throws SocketException
	{
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;
		this.socket = new SteamSocket(ipAddress, portNumber);
	}
	
	public void getChallengeNumber()
	{
		
	}
	
	public void getPing()
		throws IOException
	{
		System.out.println("Sending A2A_PING request.");
		this.socket.send(new A2A_PING_RequestPacket());
		this.socket.receive();
	}
	
	public void getServerInfo()
	{
		
	}
	
	public void initialize()
		throws IOException
	{
		this.getPing();
		this.getServerInfo();
		this.getChallengeNumber();
	}
}
