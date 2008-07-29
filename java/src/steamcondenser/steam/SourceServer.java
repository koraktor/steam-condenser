package steamcondenser.steam;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.GregorianCalendar;

import steamcondenser.steam.packets.A2A_PING_RequestPacket;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class SourceServer
{
	private int challengeNumber;

	private int ping;
	
	private SteamSocket socket;
	
	/**
	 * @param ipAddress The IP of the server to connect to
	 * @param portNumber The port number of the server
	 */
	public SourceServer(InetAddress ipAddress, int portNumber)
		throws IOException
	{
		this.socket = new SteamSocket(ipAddress, portNumber);
	}
	
	/**
	 * @return The challenge number assigned by the server
	 */
	public int getChallengeNumber()
	{
		return this.challengeNumber;
	}
	
	public void getPing()
		throws IOException, Exception
	{
		this.socket.send(new A2A_PING_RequestPacket());
		long startTime = System.currentTimeMillis();
		this.socket.getReply();
		long endTime = System.currentTimeMillis();
		this.ping = Long.valueOf(endTime - startTime).intValue();
	}
	
	public void getServerInfo()
	{
		
	}
	
	public void initialize()
		throws IOException, Exception
	{
		this.getPing();
		this.getServerInfo();
		this.getChallengeNumber();
	}
}
