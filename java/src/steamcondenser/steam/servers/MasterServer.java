/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.servers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

import steamcondenser.SteamCondenserException;
import steamcondenser.steam.packets.A2M_GET_SERVERS_BATCH2_Paket;
import steamcondenser.steam.packets.M2A_SERVER_BATCH_Paket;
import steamcondenser.steam.sockets.MasterServerSocket;

/**
 * A Steam master server
 * @author Sebastian Staudt
 * @version $Id$
 */
public class MasterServer
{
    public static final InetSocketAddress GOLDSRC_MASTER_SERVER = new InetSocketAddress("hl1master.steampowered.com", 27010);
    public static final InetSocketAddress SOURCE_MASTER_SERVER = new InetSocketAddress("hl2master.steampowered.com", 27011);

    private MasterServerSocket socket;

    public MasterServer(InetSocketAddress masterServer)
    throws IOException, UnknownHostException
    {
	this.socket = new MasterServerSocket(masterServer.getAddress(), masterServer.getPort());
    }

    public Vector<InetSocketAddress> getServers()
    throws IOException, SteamCondenserException, TimeoutException
    {
	return this.getServers(A2M_GET_SERVERS_BATCH2_Paket.REGION_ALL, "", true);
    }

    public Vector<InetSocketAddress> getServers(byte regionCode, String filter, boolean raiseTimeout)
    throws IOException, SteamCondenserException, TimeoutException
    {
	boolean finished = false;
	int portNumber = 0;
	String hostName = "0.0.0.0";
	Vector<String> serverStringArray;
	Vector<InetSocketAddress> serverArray = new Vector<InetSocketAddress>();		

	do
	{
	    this.socket.send(new A2M_GET_SERVERS_BATCH2_Paket(regionCode, hostName + ":" + portNumber, filter));
	    try {
        	serverStringArray = ((M2A_SERVER_BATCH_Paket) this.socket.getReply()).getServers();

		for (String serverString : serverStringArray) {
		    hostName = serverString.substring(0, serverString.lastIndexOf(":"));
		    portNumber = Integer.valueOf(serverString.substring(serverString.lastIndexOf(":") + 1));

		    if (hostName != "0.0.0.0" && portNumber != 0) {
			serverArray.add(new InetSocketAddress(hostName, portNumber));
		    } else {
			finished = true;
		    }
		}
	    }
	    catch(TimeoutException e)
	    {
		if(raiseTimeout)
		{
		    throw e;
		}
		finished = true;
	    }
	}
	while(!finished);

	return serverArray;
    }
}
