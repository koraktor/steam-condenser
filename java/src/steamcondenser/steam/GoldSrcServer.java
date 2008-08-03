package steamcondenser.steam;

import java.io.IOException;
import java.net.InetAddress;

public class GoldSrcServer extends GameServer
{
	/**
	 * @param ipAddress The IP of the server to connect to
	 * @param portNumber The port number of the server
	 */
	public GoldSrcServer(InetAddress ipAddress, int portNumber)
		throws IOException, Exception
	{
		super(portNumber);
		this.socket = new GoldSrcSocket(ipAddress, portNumber);
	}
}
