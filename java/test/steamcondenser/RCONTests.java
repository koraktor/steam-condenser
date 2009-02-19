/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import steamcondenser.steam.servers.GoldSrcServer;
import steamcondenser.steam.servers.SourceServer;

/**
 * @author  Sebastian Staudt
 * @version $Id$
 */
public class RCONTests
{
    /**
     * This test tries to run the "cvarlist" command over RCON on a GoldSrc server
     * @throws IOException
     * @throws TimeoutException
     * @throws SteamCondenserException
     */
    @Test
    public void rconLongGoldSrcServer()
    throws IOException, TimeoutException, SteamCondenserException
    {
	GoldSrcServer server = new GoldSrcServer(InetAddress.getByName("localhost"), 27015);
	server.rconAuth("test");
	String rconReply = server.rconExec("cvarlist");

	System.out.println(rconReply);
    }
    
    /**
     * This test tries to run the "version" command over RCON on a GoldSrc server
     * @throws IOException
     * @throws TimeoutException
     * @throws SteamCondenserException
     */
    @Test
    public void rconShortGoldSrcServer()
    throws IOException, TimeoutException, SteamCondenserException
    {
	GoldSrcServer server = new GoldSrcServer(InetAddress.getByName("localhost"), 27015);
	server.rconAuth("test");
	String rconReply = server.rconExec("version");

	System.out.println(rconReply);
    }
    
    /**
     * This test tries to run the "cvarlist" command over RCON on a Source server
     * @throws IOException
     * @throws TimeoutException
     * @throws SteamCondenserException
     */
    @Test
    public void rconLongSourceServer()
    throws IOException, TimeoutException, SteamCondenserException
    {
	SourceServer server = new SourceServer(InetAddress.getByName("localhost"), 27015);
	if(server.rconAuth("test"))
	{
	    String rconReply = server.rconExec("cvarlist");
	    System.out.println(rconReply);
	}
    }

    /**
     * This test tries to run the "version" command over RCON on a Source server
     * @throws IOException
     * @throws TimeoutException
     * @throws SteamCondenserException
     */
    @Test
    public void rconShortSourceServer()
    throws IOException, TimeoutException, SteamCondenserException
    {
	SourceServer server = new SourceServer(InetAddress.getByName("localhost"), 27015);
	if(server.rconAuth("test"))
	{
	    String rconReply = server.rconExec("version");
	    System.out.println(rconReply);
	}
    }
}
