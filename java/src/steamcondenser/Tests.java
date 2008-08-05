/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import steamcondenser.steam.servers.GoldSrcServer;
import steamcondenser.steam.servers.MasterServer;
import steamcondenser.steam.servers.SourceServer;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class Tests
{
	/**
	 * This test tries to initialize a invalid GoldSrc server
	 * @throws Exception
	 */
	@Test(expected = TimeoutException.class)
	public void invalidGoldSrcServer()
		throws Exception
	{
		GoldSrcServer invalidServer = new GoldSrcServer(InetAddress.getByName("0.0.0.0"), 27015);
		invalidServer.initialize();
	}
	
	/**
	 * This test tries to initialize a invalid Source server
	 * @throws Exception
	 */
	@Test(expected = TimeoutException.class)
	public void invalidSourceServer()
		throws Exception
	{
		SourceServer invalidServer = new SourceServer(InetAddress.getByName("0.0.0.0"), 27015);
		invalidServer.initialize();
	}
	
	/**
	 * This test gets a random GoldSrc server from the master server and does a
	 * full query on it
	 * @throws Exception
	 */
	@Test
	public void randomGoldSrcServer()
		throws Exception
	{
		Random randomizer = new Random();
		MasterServer masterServer = new MasterServer(MasterServer.GOLDSRC_MASTER_SERVER);
		Vector<InetSocketAddress> servers = masterServer.getServers();
		InetSocketAddress randomServer = servers.elementAt(randomizer.nextInt(servers.size()));
		
		GoldSrcServer server = new GoldSrcServer(randomServer.getAddress(), randomServer.getPort());
		server.initialize();
		server.updatePlayerInfo();
		server.updateRulesInfo();
		
		System.out.println(server);
	}
	
	/**
	 * This test gets a random Source server from the master server and does a
	 * full query on it
	 * @throws Exception
	 */
	@Test
	public void randomSourceServer()
		throws Exception
	{
		Random randomizer = new Random();
		MasterServer masterServer = new MasterServer(MasterServer.SOURCE_MASTER_SERVER);
		Vector<InetSocketAddress> servers = masterServer.getServers();
		InetSocketAddress randomServer = servers.elementAt(randomizer.nextInt(servers.size()));
		
		SourceServer server = new SourceServer(randomServer.getAddress(), randomServer.getPort());
		server.initialize();
		server.updatePlayerInfo();
		server.updateRulesInfo();
		
		System.out.println(server);
	}
}
