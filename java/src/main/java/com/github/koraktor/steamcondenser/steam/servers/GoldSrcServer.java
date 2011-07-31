/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.servers;

import java.net.InetAddress;
import java.util.concurrent.TimeoutException;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.sockets.GoldSrcSocket;

/**
 * This class represents a GoldSrc game server and can be used to query
 * information about and remotely execute commands via RCON on the server
 * <p/>
 * A GoldSrc game server is an instance of the Half-Life Dedicated Server
 * (HLDS) running games using Valve's GoldSrc engine, like Half-Life
 * Deathmatch, Counter-Strike 1.6 or Team Fortress Classic.
 *
 * @author Sebastian Staudt
 * @see SourceServer
 */
public class GoldSrcServer extends GameServer {

    private boolean isHLTV;

    private String rconPassword;

    /**
     * Returns a master server instance for the default master server for
     * GoldSrc games
     *
     * @return The GoldSrc master server
     * @throws SteamCondenserException if initializing the socket fails
     */
    public MasterServer getMaster() throws SteamCondenserException {
        return new MasterServer(MasterServer.GOLDSRC_MASTER_SERVER);
    }

    /**
     * Creates a new instance of a GoldSrc server object
     *
     * @param address Either an IP address, a DNS name or one of them combined
     *        with the port number. If a port number is given, e.g.
     *        'server.example.com:27016' it will override the second argument.
     * @throws SteamCondenserException if initializing the socket fails
     */
    public GoldSrcServer(String address) throws SteamCondenserException {
        this(address, 27015, false);
    }

    /**
     * Creates a new instance of a GoldSrc server object
     *
     * @param address Either an IP address, a DNS name or one of them combined
     *        with the port number. If a port number is given, e.g.
     *        'server.example.com:27016' it will override the second argument.
     * @param port The port the server is listening on
     * @throws SteamCondenserException if initializing the socket fails
     */
    public GoldSrcServer(String address, Integer port)
            throws SteamCondenserException {
        this(address, port, false);
    }

    /**
     * Creates a new instance of a GoldSrc server object
     *
     * @param address Either an IP address, a DNS name or one of them combined
     *        with the port number. If a port number is given, e.g.
     *        'server.example.com:27016' it will override the second argument.
     * @param port The port the server is listening on
     * @param isHLTV HLTV servers need special treatment, so this is used to
     *        determine if the server is a HLTV server
     * @throws SteamCondenserException if initializing the socket fails
     */
    public GoldSrcServer(String address, Integer port, boolean isHLTV)
            throws SteamCondenserException {
        super(address, port);

        this.isHLTV = isHLTV;
    }

    /**
     * Creates a new instance of a GoldSrc server object
     *
     * @param address Either an IP address, a DNS name or one of them combined
     *        with the port number. If a port number is given, e.g.
     *        'server.example.com:27016' it will override the second argument.
     * @throws SteamCondenserException if initializing the socket fails
     */
    public GoldSrcServer(InetAddress address) throws SteamCondenserException {
        this(address, 27015, false);
    }

    /**
     * Creates a new instance of a GoldSrc server object
     *
     * @param address Either an IP address, a DNS name or one of them combined
     *        with the port number. If a port number is given, e.g.
     *        'server.example.com:27016' it will override the second argument.
     * @param port The port the server is listening on
     * @throws SteamCondenserException if initializing the socket fails
     */
    public GoldSrcServer(InetAddress address, Integer port)
            throws SteamCondenserException {
        this(address.toString(), port, false);
    }

    /**
     * Creates a new instance of a GoldSrc server object
     *
     * @param address Either an IP address, a DNS name or one of them combined
     *        with the port number. If a port number is given, e.g.
     *        'server.example.com:27016' it will override the second argument.
     * @param port The port the server is listening on
     * @param isHLTV HLTV servers need special treatment, so this is
     *        used to determine if the server is a HLTV server
     * @throws SteamCondenserException if initializing the socket fails
     */
    public GoldSrcServer(InetAddress address, Integer port, boolean isHLTV)
            throws SteamCondenserException {
        this(address.toString(), port, isHLTV);
    }

    /**
     * Initializes the socket to communicate with the GoldSrc server
     *
     * @see GoldSrcSocket
     */
    public void initSocket() throws SteamCondenserException {
        this.socket = new GoldSrcSocket(this.ipAddress, this.port, this.isHLTV);
    }

    /**
     * Saves the password for authenticating the RCON communication with the
     * server
     *
     * @param password The RCON password of the server
     * @return GoldSrc's RCON does not preauthenticate connections so
     *         this method always returns <code>true</code>
     * @see #rconExec
     */
    public boolean rconAuth(String password) {
        this.rconPassword = password;

        return true;
    }

    /**
     * Remotely executes a command on the server via RCON
     *
     * @param command The command to execute on the server via RCON
     * @return The output of the executed command
     * @see #rconExec
     * @throws SteamCondenserException if the request fails
     * @throws TimeoutException if the request times out
     */
    public String rconExec(String command)
            throws TimeoutException, SteamCondenserException {
        return ((GoldSrcSocket) this.socket).rconExec(this.rconPassword, command).trim();
    }

}
