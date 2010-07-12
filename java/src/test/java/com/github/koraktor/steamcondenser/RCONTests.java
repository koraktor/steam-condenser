/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */
package com.github.koraktor.steamcondenser;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.servers.GoldSrcServer;
import com.github.koraktor.steamcondenser.steam.servers.SourceServer;

/**
 * @author  Sebastian Staudt
 */
public class RCONTests
{
    private InetAddress goldSrcServerAddress;
    private int goldSrcServerPort;
    private InetAddress sourceServerAddress;
    private int sourceServerPort;

    /**
     * Setup method to load fixtures from test-fixtures.properties
     * @throws java.net.UnknownHostException
     */
    @Before
    public void setUp()
        throws UnknownHostException
    {
        Properties testFixtures = new Properties();
        try {
            testFixtures.load(new FileInputStream("test-fixtures.properties"));
        }
        catch(IOException e) {}

        if(testFixtures.containsKey("goldSrcServerAddress")) {
            this.goldSrcServerAddress = InetAddress.getByName(testFixtures.getProperty("goldSrcServerAddress"));
        }
        else {
            this.goldSrcServerAddress = InetAddress.getLocalHost();
        }

        if(testFixtures.containsKey("goldSrcServerPort")) {
            this.goldSrcServerPort = Integer.valueOf(testFixtures.getProperty("goldSrcServerPort"));
        }
        else {
            this.goldSrcServerPort = 27015;
        }

        if(testFixtures.containsKey("sourceServerAddress")) {
            this.sourceServerAddress = InetAddress.getByName(testFixtures.getProperty("sourceServerAddress"));
        }
        else {
            this.sourceServerAddress = InetAddress.getLocalHost();
        }

        if(testFixtures.containsKey("sourceServerPort")) {
            this.sourceServerPort = Integer.valueOf(testFixtures.getProperty("sourceServerPort"));
        }
        else {
            this.sourceServerPort = 27015;
        }
    }

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
        GoldSrcServer server = new GoldSrcServer(this.goldSrcServerAddress, this.goldSrcServerPort);
        server.rconAuth("test");
        String rconReply = server.rconExec("cvarlist");

        System.out.println(rconReply);

        Assert.assertTrue(
                "Did not receive complete cvarlist.",
                rconReply.contains("CvarList ? for syntax"));
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
        SourceServer server = new SourceServer(this.sourceServerAddress, this.sourceServerPort);
        if (server.rconAuth("test")) {
            String rconReply = server.rconExec("cvarlist");
            System.out.println(rconReply);

            Assert.assertTrue(
                "Did not receive complete cvarlist.",
                rconReply.contains("total convars/concommands"));
        }
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
        GoldSrcServer server = new GoldSrcServer(this.goldSrcServerAddress, this.goldSrcServerPort);
        server.rconAuth("test");
        String rconReply = server.rconExec("version");

        System.out.println(rconReply);

        Assert.assertTrue(
                "Did not receive correct version response.",
                rconReply.contains("Protocol version") &&
                rconReply.contains("Exe version") &&
                rconReply.contains("Exe build"));
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
        SourceServer server = new SourceServer(this.sourceServerAddress, this.sourceServerPort);
        if (server.rconAuth("test")) {
            String rconReply = server.rconExec("version");
            System.out.println(rconReply);

            Assert.assertTrue(
                "Did not receive correct version response.",
                rconReply.contains("Protocol version") &&
                rconReply.contains("Exe version") &&
                rconReply.contains("Exe build"));
        }
    }
}
