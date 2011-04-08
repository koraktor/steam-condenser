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
 *
 * It provides basic name resolution features.
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
     * @param address
     * @param port
     * @throws IOException
     * @throws SteamCondenserException
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

    public List<String> getHostNames() {
        return this.hostNames;
    }

    public List<InetAddress> getIpAddresses() {
        return this.ipAddresses;
    }

    /**
     * Rotate this server's IP address to the next one in the IP list
     *
     * This method will return <code>true</code>, if the IP list reached its
     * end. If the list contains only one IP address, this method will
     * instantly return <code>true</code>.
     *
     * @return bool
     * @throws IOException
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
     *
     * @throws IOException
     */
    protected abstract void initSocket() throws IOException;

}
