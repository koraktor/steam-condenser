package steamcondenser;

import java.net.InetAddress;

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
		SourceServer server = new SourceServer(InetAddress.getByName("84.45.77.22"), 27045);
		server.initialize();
		server.updatePlayerInfo();
		server.updateRulesInfo();
		
		System.out.println(server);
	}
}
