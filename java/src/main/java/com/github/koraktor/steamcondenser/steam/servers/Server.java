/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.servers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;

/**
 * This class is subclassed by all classes implementing server functionality
 * <p/>
 * It provides basic name resolution features and the ability to rotate
 * between different IP addresses belonging to a single DNS name.
 *
 * @author Sebastian Staudt
 */
public abstract class Server {

    private List<String> hostNames;

    protected InetAddress ipAddress;

    private List<InetAddress> ipAddresses;

    protected int ipIndex;

    protected int port;

    /**
     * Creates a new server instance with the given address and port
     *
     * @param address Either an IP address, a DNS name or one of them combined
     *        with the port number. If a port number is given, e.g.
     *        'server.example.com:27016' it will override the second argument.
     * @param port The port the server is listening on
     * @see #initSocket
     * @throws IOException if initializing the socket fails
     * @throws SteamCondenserException if an host name cannot be resolved
     */
    protected Server(String address, Integer port)
            throws IOException, SteamCondenserException {
        if(address.indexOf(':') >= 0) {
            String[] tmpAddress = address.split(":", 2);
            port    = Integer.parseInt(tmpAddress[1]);
            address = tmpAddress[0];
        }

        if(port == null) {
            port = 27015;
        }

        this.hostNames   = new ArrayList<String>();
        this.ipAddresses = new ArrayList<InetAddress>();
        this.ipIndex     = 0;
        this.port        = port;

        try {
            for(InetAddress ipAddress : InetAddress.getAllByName(address)) {
                this.hostNames.add(ipAddress.getHostName());
                this.ipAddresses.add(ipAddress);
            }
        } catch(UnknownHostException e) {
            throw new SteamCondenserException("Cannot resolve " + address + ": " + e.getMessage());
        }

        this.ipAddress = this.ipAddresses.get(0);

        this.initSocket();
    }

    /**
     * Returns a list of host names associated with this server
     *
     * @return The host names of this server
     */
    public List<String> getHostNames() {
        return this.hostNames;
    }

    /**
     * Returns a list of IP addresses associated with this server
     *
     * @return The IP addresses of this server
     */
    public List<InetAddress> getIpAddresses() {
        return this.ipAddresses;
    }

    /**
     * Rotate this server's IP address to the next one in the IP list
     * <p/>
     * If this method returns <code>true</code>, it indicates that all IP
     * addresses have been used, hinting at the server(s) being unreachable. An
     * appropriate action should be taken to inform the user.
     * <p/>
     * Servers with only one IP address will always cause this method to return
     * <code>true</code> and the sockets will not be reinitialized.
     * <p/>
     * @return bool <code>true</code>, if the IP list reached its end. If the
     *         list contains only one IP address, this method will instantly
     *         return <code>true</code>
     * @see #initSocket
     * @throws IOException if initializing the socket fails
     */
    public boolean rotateIp() throws IOException {
        if(this.ipAddresses.size() == 1) {
            return true;
        }

        this.ipIndex   = (this.ipIndex + 1) % this.ipAddresses.size();
        this.ipAddress = this.ipAddresses.get(this.ipIndex);

        this.initSocket();

        return this.ipIndex == 0;
    }

    /**
     * Initializes the socket(s) to communicate with the server
     * <p/>
     * Must be implemented in subclasses to prepare sockets for server
     * communication
     *
     * @throws IOException if initializing the socket fails
     */
    protected abstract void initSocket() throws IOException;

}
