package steamcondenser.steam;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

import steamcondenser.steam.packets.A2A_INFO_RequestPacket;
import steamcondenser.steam.packets.A2A_INFO_ResponsePacket;
import steamcondenser.steam.packets.A2A_PING_RequestPacket;
import steamcondenser.steam.packets.A2A_PLAYER_RequestPacket;
import steamcondenser.steam.packets.A2A_PLAYER_ResponsePacket;
import steamcondenser.steam.packets.A2A_RULES_RequestPacket;
import steamcondenser.steam.packets.A2A_RULES_ResponsePacket;
import steamcondenser.steam.packets.A2A_SERVERQUERY_GETCHALLENGE_RequestPacket;
import steamcondenser.steam.packets.A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class SourceServer
{
	private int challengeNumber = 0xFFFFFFFF;

	private int ping;
	
	private ArrayList<SteamPlayer> playerArray;
	
	private HashMap<String, String> rulesHash;
	
	private HashMap<String, Object> serverInfo;
	
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
	public void getChallengeNumber()
		throws IOException, Exception
	{
		this.socket.send(new A2A_SERVERQUERY_GETCHALLENGE_RequestPacket());
		this.challengeNumber = ((A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket) this.socket.getReply()).getChallengeNumber();
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
	
	public void getPlayerInfo()
		throws IOException, Exception
	{
		this.socket.send(new A2A_PLAYER_RequestPacket(this.challengeNumber));
		this.playerArray = ((A2A_PLAYER_ResponsePacket) this.socket.getReply()).getPlayerArray();
	}
	
	public void getRulesInfo()
		throws IOException, Exception
	{
		this.socket.send(new A2A_RULES_RequestPacket(this.challengeNumber));
		this.rulesHash = ((A2A_RULES_ResponsePacket) this.socket.getReply()).getRulesHash();
	}
	
	public void getServerInfo()
		throws IOException, Exception
	{
		this.socket.send(new A2A_INFO_RequestPacket());
		this.serverInfo = ((A2A_INFO_ResponsePacket) this.socket.getReply()).getInfoHash();
	}
	
	public void initialize()
		throws IOException, Exception
	{
		this.getPing();
		this.getServerInfo();
		this.getChallengeNumber();
	}
}
