package steamcondenser;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import sip.steam.SourceServer;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class Test
{
	public static void main(String[] argv)
		throws IOException
	{
		try
		{
			SourceServer server = new SourceServer(InetAddress.getByName("84.45.77.22"), 27045);
			server.initialize();
		}
		catch(UnknownHostException e)
		{
			System.err.println(e);
		}
	}
}
