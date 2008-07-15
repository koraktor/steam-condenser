package steamcondenser.steam;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import steamcondeser.steam.packets.A2A_PING_RequestPacket;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class SourceServer
{
	private InetAddress ipAddress;
	
	private int portNumber;
	
	private SteamSocket socket;
	
	/**
	 * @param ipAddress The IP of the server to connect to
	 * @param portNumber The port number of the server
	 */
	public SourceServer(InetAddress ipAddress, int portNumber)
		throws SocketException
	{
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;
		this.socket = new SteamSocket(ipAddress, portNumber);
	}
	
	/**
	 * @return The challenge number assigned by the server
	 */
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
