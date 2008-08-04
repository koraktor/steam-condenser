package steamcondenser;

import java.net.InetSocketAddress;
import java.util.Random;
import java.util.Vector;

import steamcondenser.steam.servers.GameServer;
import steamcondenser.steam.servers.GoldSrcServer;
import steamcondenser.steam.servers.MasterServer;
import steamcondenser.steam.servers.SourceServer;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class Test
{
	public static void main(String[] argv)
		throws Exception
	{
		GameServer server;
		MasterServer masterServer;
		Random randomizer = new Random();
		Vector<InetSocketAddress> servers;
		InetSocketAddress randomServer;
		
		masterServer = new MasterServer(MasterServer.SOURCE_MASTER_SERVER);
		servers = masterServer.getServers();
		randomServer = servers.elementAt(randomizer.nextInt(servers.size()));
		
		server = new SourceServer(randomServer.getAddress(), randomServer.getPort());
		server.initialize();
		server.updatePlayerInfo();
		server.updateRulesInfo();
		
		System.out.println(server);
		
		masterServer = new MasterServer(MasterServer.GOLDSRC_MASTER_SERVER);
		servers = masterServer.getServers();
		randomServer = servers.elementAt(randomizer.nextInt(servers.size()));
		
		server = new GoldSrcServer(randomServer.getAddress(), randomServer.getPort());
		server.initialize();
		server.updatePlayerInfo();
		server.updateRulesInfo();
		
		System.out.println(server);
	}
}
