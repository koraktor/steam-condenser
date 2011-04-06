/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.servers;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.sockets.GoldSrcSocket;

/**
 * A GoldSrc game server.
 *
 * @author Sebastian Staudt
 */
public class GoldSrcServer extends GameServer {

	private String rconPassword;

	/**
	 * @param ipAddress The IP of the server to connect to
	 * @param portNumber The port number of the server
	 */
	public GoldSrcServer(InetAddress ipAddress, int portNumber)
			throws IOException {
		super(portNumber);
		this.socket = new GoldSrcSocket(ipAddress, portNumber, false);
	}

	/**
	 * @param ipAddress The IP of the server to connect to
	 * @param portNumber The port number of the server
	 * @param isHLTV Whether this server is a HLTV server
	 */
	public GoldSrcServer(InetAddress ipAddress, int portNumber, boolean isHLTV)
			throws IOException {
		super(portNumber);
		this.socket = new GoldSrcSocket(ipAddress, portNumber, isHLTV);
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
