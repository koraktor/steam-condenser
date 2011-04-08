/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.servers;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeoutException;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.sockets.GoldSrcSocket;

/**
 * A GoldSrc game server.
 *
 * @author Sebastian Staudt
 */
public class GoldSrcServer extends GameServer {

    private boolean isHLTV;

	private String rconPassword;

    /**
     * @param address The address of the server to connect to
     * @throws IOException
     * @throws SteamCondenserException
     */
    public GoldSrcServer(String address)
            throws IOException, SteamCondenserException {
        this(address, 27015, false);
    }

    /**
     * @param address The address of the server to connect to
     * @param port The port number of the server
     * @throws IOException
     * @throws SteamCondenserException
     */
    public GoldSrcServer(String address, Integer port)
            throws IOException, SteamCondenserException {
        this(address, port, false);
    }

    /**
     * @param address The address of the server to connect to
     * @param port The port number of the server
     * @param isHLTV Whether this server is a HLTV server
     * @throws IOException
     * @throws SteamCondenserException
     */
    public GoldSrcServer(String address, Integer port, boolean isHLTV)
            throws IOException, SteamCondenserException {
        super(address, port);

        this.isHLTV = isHLTV;
    }

    /**
     * @param address The address of the server to connect to
     * @throws IOException
     * @throws SteamCondenserException
     */
    public GoldSrcServer(InetAddress address)
            throws IOException, SteamCondenserException {
        this(address, 27015, false);
    }

	/**
	 * @param ipAddress The IP of the server to connect to
     * @param port The port number of the server
     * @throws IOException
     * @throws SteamCondenserException
	 */
    public GoldSrcServer(InetAddress ipAddress, Integer port)
            throws IOException, SteamCondenserException {
        this(ipAddress.toString(), port, false);
    }

	/**
	 * @param ipAddress The IP of the server to connect to
     * @param port The port number of the server
	 * @param isHLTV Whether this server is a HLTV server
     * @throws IOException
     * @throws SteamCondenserException
	 */
    public GoldSrcServer(InetAddress ipAddress, Integer port, boolean isHLTV)
            throws IOException, SteamCondenserException {
        this(ipAddress.toString(), port, isHLTV);
    }

    /**
     * Initializes the socket to communicate with the GoldSrc server
     */
    public void initSocket() throws IOException {
        this.socket = new GoldSrcSocket(this.ipAddress, this.port, this.isHLTV);
    }

	/**
	 * @param password Password to use for RCON commands
	 * @return Returns always true, because GoldSrc doesn't have a special
	 *         authentication feature
	 */
	public boolean rconAuth(String password) {
		this.rconPassword = password;
		return true;
	}

	/**
	 * @param command RCON command to send to the server
	 * @return The response send by the server
	 * @throws SteamCondenserException
	 */
	public String rconExec(String command)
			throws IOException, TimeoutException, SteamCondenserException {
		return ((GoldSrcSocket) this.socket).rconExec(this.rconPassword, command).trim();
	}

}
