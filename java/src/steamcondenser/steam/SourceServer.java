package steamcondenser.steam;

import java.io.IOException;
import java.net.InetAddress;

public class SourceServer extends GameServer
{
	/**
	 * @param ipAddress The IP of the server to connect to
	 * @param portNumber The port number of the server
	 */
	public SourceServer(InetAddress ipAddress, int portNumber)
		throws IOException, Exception
	{
		super(portNumber);
		this.socket = new SourceSocket(ipAddress, portNumber);
	}
}
