/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.servers;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import com.github.koraktor.steamcondenser.exceptions.RCONNoAuthException;
import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.packets.rcon.RCONAuthRequestPacket;
import com.github.koraktor.steamcondenser.steam.packets.rcon.RCONAuthResponse;
import com.github.koraktor.steamcondenser.steam.packets.rcon.RCONExecRequestPacket;
import com.github.koraktor.steamcondenser.steam.packets.rcon.RCONExecResponsePacket;
import com.github.koraktor.steamcondenser.steam.packets.rcon.RCONPacket;
import com.github.koraktor.steamcondenser.steam.packets.rcon.RCONTerminator;
import com.github.koraktor.steamcondenser.steam.sockets.RCONSocket;
import com.github.koraktor.steamcondenser.steam.sockets.SourceSocket;

/**
 * This class represents a Source game server and can be used to query
 * information about and remotely execute commands via RCON on the server
 * <p/>
 * A Source game server is an instance of the Source Dedicated Server (SrcDS)
 * running games using Valve's Source engine, like Counter-Strike: Source,
 * Team Fortress 2 or Left4Dead.
 *
 * @author Sebastian Staudt
 * @see GoldSrcServer
 */
public class SourceServer extends GameServer {

    protected RCONSocket rconSocket;

    /**
     * Returns a master server instance for the default master server for
     * Source games
     *
     * @return The Source master server
     * @throws SteamCondenserException if initializing the socket fails
     */
    public MasterServer getMaster() throws SteamCondenserException {
        return new MasterServer(MasterServer.SOURCE_MASTER_SERVER);
    }

    /**
     * Creates a new instance of a server object representing a Source server,
     * i.e. SrcDS instance
     *
     * @param address Either an IP address, a DNS name or one of them
     *        combined with the port number. If a port number is given, e.g.
     *        'server.example.com:27016' it will override the second argument.
     * @throws SteamCondenserException if initializing the socket fails
     */
    public SourceServer(String address) throws SteamCondenserException {
        super(address, 27015);
    }

    /**
     * Creates a new instance of a server object representing a Source server,
     * i.e. SrcDS instance
     *
     * @param address Either an IP address, a DNS name or one of them
     *        combined with the port number. If a port number is given, e.g.
     *        'server.example.com:27016' it will override the second argument.
     * @param port The port the server is listening on
     * @throws SteamCondenserException if initializing the socket fails
     */
    public SourceServer(String address, Integer port)
            throws SteamCondenserException {
        super(address, port);
    }

    /**
     * Creates a new instance of a server object representing a Source server,
     * i.e. SrcDS instance
     *
     * @param address Either an IP address, a DNS name or one of them
     *        combined with the port number. If a port number is given, e.g.
     *        'server.example.com:27016' it will override the second argument.
     * @throws SteamCondenserException if initializing the socket fails
     */
    public SourceServer(InetAddress address) throws SteamCondenserException {
        super(address.toString(), 27015);
    }

    /**
     * Creates a new instance of a server object representing a Source server,
     * i.e. SrcDS instance
     *
     * @param address Either an IP address, a DNS name or one of them
     *        combined with the port number. If a port number is given, e.g.
     *        'server.example.com:27016' it will override the second argument.
     * @param port The port the server is listening on
     * @throws SteamCondenserException if initializing the socket fails
     */
    public SourceServer(InetAddress address, Integer port)
            throws SteamCondenserException {
        super(address.toString(), port);
    }

    /**
     * Initializes the sockets to communicate with the Source server
     *
     * @see RCONSocket
     * @see SourceSocket
     */
    public void initSocket() throws SteamCondenserException {
        this.rconSocket = new RCONSocket(this.ipAddress, this.port);
        this.socket = new SourceSocket(this.ipAddress, this.port);
    }

    /**
     * Authenticates the connection for RCON communication with the server
     *
     * @param password The RCON password of the server
     * @return whether authentication was successful
     * @see #rconAuth
     * @throws SteamCondenserException if the request fails
     * @throws TimeoutException if the request times out
     */
    public boolean rconAuth(String password)
            throws TimeoutException, SteamCondenserException {
        this.rconRequestId = new Random().nextInt();

        this.rconSocket.send(new RCONAuthRequestPacket(this.rconRequestId, password));
        this.rconSocket.getReply();
        RCONAuthResponse reply = (RCONAuthResponse) this.rconSocket.getReply();
        this.rconAuthenticated = reply.getRequestId() == this.rconRequestId;

        return this.rconAuthenticated;
    }

    /**
     * Remotely executes a command on the server via RCON
     *
     * @param command The command to execute on the server via RCON
     * @return The output of the executed command
     * @see #rconExec
     * @throws SteamCondenserException if a problem occurs while parsing the
     *         reply
     * @throws TimeoutException if the request times out
     */
    public String rconExec(String command)
            throws TimeoutException, SteamCondenserException {
        if(!this.rconAuthenticated) {
            throw new RCONNoAuthException();
        }

        this.rconSocket.send(new RCONExecRequestPacket(this.rconRequestId, command));
        this.rconSocket.send(new RCONTerminator(this.rconRequestId));
        RCONPacket responsePacket;

        String response = "";
        do {
            responsePacket = this.rconSocket.getReply();
            if(responsePacket instanceof RCONAuthResponse) {
                this.rconAuthenticated = false;
                throw new RCONNoAuthException();
            }
            response += ((RCONExecResponsePacket) responsePacket).getResponse();
        } while(response.length() == 0 || ((RCONExecResponsePacket) responsePacket).getResponse().length() > 0);

        return response.trim();
    }

}
