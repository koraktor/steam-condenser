package steamcondenser.steam.servers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

import steamcondenser.SteamCondenserException;
import steamcondenser.steam.packets.MasterServerQueryRequestPacket;
import steamcondenser.steam.packets.MasterServerQueryResponsePacket;
import steamcondenser.steam.sockets.MasterServerSocket;

public class MasterServer
{
	public static final String GOLDSRC_MASTER_SERVER = "hl1master.steampowered.com";
	public static final String SOURCE_MASTER_SERVER = "hl2master.steampowered.com";
	
	private MasterServerSocket socket;
	
	public MasterServer(String hostName)
		throws IOException, UnknownHostException
	{
		this.socket = new MasterServerSocket(InetAddress.getByName(hostName), 27011);
	}
	
	public Vector<InetSocketAddress> getServers()
		throws IOException, SteamCondenserException, TimeoutException
	{
		return this.getServers(MasterServerQueryRequestPacket.REGION_ALL, "");
	}
	
	public Vector<InetSocketAddress> getServers(byte regionCode, String filter)
		throws IOException, SteamCondenserException, TimeoutException
	{
		boolean finished = false;
		int portNumber = 0;
		String hostName = "0.0.0.0";
		Vector<String> serverStringArray;
		Vector<InetSocketAddress> serverArray = new Vector<InetSocketAddress>();		
		
		do
		{
			this.socket.send(new MasterServerQueryRequestPacket(regionCode, hostName + ":" + portNumber, filter));
			serverStringArray = ((MasterServerQueryResponsePacket) this.socket.getReply()).getServers();
			
			for(String serverString : serverStringArray)
			{
				hostName = serverString.substring(0, serverString.lastIndexOf(":"));
				portNumber = Integer.valueOf(serverString.substring(serverString.lastIndexOf(":") + 1));
				
				if(hostName != "0.0.0.0" && portNumber != 0)
				{
					serverArray.add(new InetSocketAddress(hostName, portNumber));
				}
				else
				{
					finished = true;
				}
			}
		}
		while(!finished);
		
		return serverArray;
	}
}
