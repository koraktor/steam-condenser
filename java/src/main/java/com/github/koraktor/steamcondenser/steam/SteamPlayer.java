/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam;

import java.util.List;
import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;

/**
 * A player on a GameServer
 * @author Sebastian Staudt
 */
public class SteamPlayer {

    private int clientPort;
    private float connectTime;
    private boolean extended;
    private int id;
    private String ipAddress;
    private int loss;
    private String name;
    private int ping;
    private int realId;
    private int score;
    private String state;
    private String steamId;

    /**
     * Creates a new SteamPlayer object with the given information
     * @param id The ID of the player
     * @param name The nickname of the player
     * @param score The score of the player
     * @param connectTime The time the player is connected to the server
     */
    public SteamPlayer(int id, String name, int score, float connectTime) {
        this.connectTime = connectTime;
        this.id = id;
        this.name = name;
        this.score = score;
        this.extended = false;
    }

    public void addInformation(List<String> playerData)
            throws SteamCondenserException {
        this.extended = true;

        this.realId = Integer.parseInt(playerData.get(0));
        this.steamId = playerData.get(2);

        if(!playerData.get(1).equals(this.name)) {
            throw new SteamCondenserException("Information to add belongs to a different player.");
        }

        if(this.steamId.equals("BOT")) {
            this.state = playerData.get(3);
        } else {
            String[] address = playerData.get(6).split(":");
            this.ipAddress   = address[0];
            this.clientPort  = Integer.parseInt(address[1]);
            this.loss        = Integer.parseInt(playerData.get(4));
            this.ping        = Integer.parseInt(playerData.get(3));
            this.state       = playerData.get(5);
        }
    }

    /**
     * Returns the client port of this player
     * @return int
     */
    public int getClientPort()
    {
        return this.clientPort;
    }

    /**
     * @return Returns the time this player is connected to the server
     */
    public float getConnectTime() {
        return this.connectTime;
    }

    /**
     * @return Returns the ID of this player
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return Returns the IP address this player
     */
    public String getIpAddress() {
        return this.ipAddress;
    }

    /**
     * @return Returns the packet loss of this player
     */
    public int getLoss() {
        return this.loss;
    }

    /**
     * @return Returns the nickname of this player
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return Returns the ping of this player
     */
    public int getPing() {
        return this.ping;
    }

    /**
     * Returns the real ID (as used on the server) of this player
     */
    public int getRealId()
    {
        return this.realId;
    }

    /**
     * @return Returns the score of this player
     */
    public int getScore() {
        return this.score;
    }

    /**
     * @return Returns the connection state of this player
     */
    public String getState() {
        return this.state;
    }

    /**
     * Returns the SteamID of this player
     */
    public String getSteamId()
    {
        return this.steamId;
    }

    /**
     * Returns whether this player object has extended information
     */
    public boolean isExtended()
    {
        return this.extended;
    }

    /**
     * @return A String representation of this player
     */
    @Override
    public String toString() {
        if(this.extended) {
            return "#" + this.realId + " \"" + this.name + "\", SteamID: " + this.steamId + ", Score: " + this.score + ", Time: " + this.connectTime;
        }
        else {
            return "#" + this.id + " \"" + this.name + "\", Score: " + this.score + ", Time: " + this.connectTime;
        }
    }
}
