package steamcondenser;

import java.net.InetAddress;

import steamcondenser.steam.GameServer;
import steamcondenser.steam.GoldSrcServer;
import steamcondenser.steam.SourceServer;

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
		
		server = new SourceServer(InetAddress.getByName("84.45.77.22"), 27045);
		server.initialize();
		server.updatePlayerInfo();
		server.updateRulesInfo();
		
		System.out.println(server);
		
		server = new GoldSrcServer(InetAddress.getByName("84.254.65.216"), 27040);
		server.initialize();
		server.updatePlayerInfo();
		server.updateRulesInfo();
		
		System.out.println(server);
	}
}
