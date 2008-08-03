package steamcondenser.steam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import steamcondenser.steam.packets.A2A_INFO_RequestPacket;
import steamcondenser.steam.packets.A2A_INFO_ResponsePacket;
import steamcondenser.steam.packets.A2A_PING_RequestPacket;
import steamcondenser.steam.packets.A2A_PLAYER_RequestPacket;
import steamcondenser.steam.packets.A2A_PLAYER_ResponsePacket;
import steamcondenser.steam.packets.A2A_RULES_RequestPacket;
import steamcondenser.steam.packets.A2A_RULES_ResponsePacket;
import steamcondenser.steam.packets.A2A_SERVERQUERY_GETCHALLENGE_RequestPacket;
import steamcondenser.steam.packets.A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket;
import steamcondenser.steam.packets.SteamPacket;

/**
 * @author Sebastian Staudt
 * @version $Id: SourceServer.java 76 2008-08-01 12:20:16Z koraktor $
 */
public class GameServer
{
	protected int challengeNumber = 0xFFFFFFFF;

	protected int ping;
	
	protected ArrayList<SteamPlayer> playerArray;
	
	protected HashMap<String, String> rulesHash;
	
	protected HashMap<String, Object> serverInfo;
	
	protected SteamSocket socket;
	
	/**
	 * @param ipAddress The IP of the server to connect to
	 * @param portNumber The port number of the server
	 */
	protected GameServer(int portNumber)
		throws Exception
	{
		if(portNumber < 0)
		{
			throw new Exception("The listening port of the server has to be a number greater than 0.");
		}
	}
	
	/**
	 * @return The response time of this server in milliseconds
	 */
	public int getPing()
	{
		return this.ping;
	}
	
	/**
	 * @return An ArrayList of SteamPlayers representing all players on this server
	 */
	public ArrayList<SteamPlayer> getPlayers()
	{
		return this.playerArray;
	}
	
	/**
	 * @return A HashMap containing the rules of this server
	 */
	public HashMap<String, String> getRules()
	{
		return this.rulesHash;
	}
	
	/**
	 * @return A HashMap containing basic information about the server
	 */
	public HashMap<String, Object> getServerInfo()
	{
		return this.serverInfo;
	}
	
	public void initialize()
	throws IOException, Exception
	{
		this.updatePing();
		this.updateServerInfo();
		this.updateChallengeNumber();
	}
	
	public void updateChallengeNumber()
		throws IOException, Exception
	{
		this.sendRequest(new A2A_SERVERQUERY_GETCHALLENGE_RequestPacket());
		this.challengeNumber = ((A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket) this.getReply()).getChallengeNumber();
	}
	
	public void updatePing()
		throws IOException, Exception
	{
		this.sendRequest(new A2A_PING_RequestPacket());
		long startTime = System.currentTimeMillis();
		this.getReply();
		long endTime = System.currentTimeMillis();
		this.ping = Long.valueOf(endTime - startTime).intValue();
	}
	
	public void updatePlayerInfo()
		throws IOException, Exception
	{
		this.sendRequest(new A2A_PLAYER_RequestPacket(this.challengeNumber));
		this.playerArray = ((A2A_PLAYER_ResponsePacket) this.getReply()).getPlayerArray();
	}
	
	public void updateRulesInfo()
		throws IOException, Exception
	{
		this.sendRequest(new A2A_RULES_RequestPacket(this.challengeNumber));
		this.rulesHash = ((A2A_RULES_ResponsePacket) this.getReply()).getRulesHash();
	}
	
	public void updateServerInfo()
		throws IOException, Exception
	{
		this.sendRequest(new A2A_INFO_RequestPacket());
		this.serverInfo = ((A2A_INFO_ResponsePacket) this.getReply()).getInfoHash();
	}
	
	private SteamPacket getReply()
		throws Exception
	{
		return this.socket.getReply();
	}
	
	private void sendRequest(SteamPacket requestData)
		throws IOException
	{
		this.socket.send(requestData);
	}
	
	public String toString()
	{
		String returnString = "";
		
		returnString += "Ping: " + this.ping + "\n";
		returnString += "Challenge number: " + this.challengeNumber + "\n";
		
		if(this.serverInfo != null)
		{
			returnString += "Info:" + "\n";
			for(Entry<String, Object> info : this.serverInfo.entrySet())
			{
				returnString += "  " + info.getKey() + ": " + info.getValue() + "\n";
			}
		}
		
		if(this.playerArray != null)
		{
			returnString += "Players:" + "\n";
			for(SteamPlayer player : this.playerArray)
			{
				returnString += "  " + player + "\n";
			}
		}
		
		if(this.rulesHash != null)
		{
			returnString += "Rules:" + "\n";
			for(Entry<String, String> rule : this.rulesHash.entrySet())
			{
				returnString += "  " + rule.getKey() + ": " + rule.getValue() + "\n";
			}
		}
		
		return returnString;
	}
}
