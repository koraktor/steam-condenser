/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */
package com.github.koraktor.steamcondenser.steam.packets;

import com.github.koraktor.steamcondenser.steam.servers.MasterServer;

/**
 * Represents a request sent to a master server.
 * <br /><br />
 * <a name="filtering">Filtering</a>:<br />
 * Instead of filtering the results sent by the master server locally, you
 * should at least use the following filters to narrow down the results sent by
 * the master server. Receiving all servers from the master server is taking
 * quite some time.<br /><br />
 * Available filters:
 * <ul>
 * 	<li>\type\d: Request only dedicated servers</li>
 *  <li>\secure\1: Request only secure servers</li>
 *  <li>\gamedir\[mod]: Request only servers of a specific mod</li>
 *  <li>\map\[mapname]: Request only servers running a specific map</li>
 *  <li>\linux\1: Request only linux servers</li>
 *  <li>\emtpy\1: Request only <b>non</b>-empty servers</li>
 *  <li>\full\1: Request only servers <b>not</b> full</li>
 *  <li>\proxy\1: Request only spectator proxy servers</li>
 * </ul>
 * @author Sebastian Staudt
 */
public class A2M_GET_SERVERS_BATCH2_Paket extends SteamPacket
{
    private String filter;
    private byte regionCode;
    private String startIp;

    /**
     * Creates a master server request without applying any filters.
     */
    public A2M_GET_SERVERS_BATCH2_Paket()
    {
        this(MasterServer.REGION_ALL, "0.0.0.0:0", "");
    }

    /**
     * Creates a master server request, filtering by the given paramters.
     * @param regionCode The region code to filter servers by region.
     * @param startIp This should be the last IP received from the master
     *        server or 0.0.0.0
     * @param filter The <a href="#filtering">filters</a> to apply in the form ("\filtername\value...")
     */
    public A2M_GET_SERVERS_BATCH2_Paket(byte regionCode, String startIp, String filter)
    {
        super(SteamPacket.A2M_GET_SERVERS_BATCH2_HEADER);

        this.filter = filter;
        this.regionCode = regionCode;
        this.startIp = startIp;
    }

    /**
     * @return A byte[] representing the contents of this request packet
     */
    public byte[] getBytes()
    {
        byte[] bytes, filterBytes, startIpBytes;

        filterBytes = (this.filter + "\0").getBytes();
        startIpBytes = (this.startIp + "\0").getBytes();
        bytes = new byte[2 + startIpBytes.length + filterBytes.length];

        bytes[0] = this.headerData;
        bytes[1] = this.regionCode;
        System.arraycopy(startIpBytes, 0, bytes, 2, startIpBytes.length);
        System.arraycopy(filterBytes, 0, bytes, startIpBytes.length + 2, filterBytes.length);

        return bytes;
    }
}
