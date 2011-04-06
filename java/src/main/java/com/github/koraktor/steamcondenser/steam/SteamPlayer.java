/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam;

import java.util.List;
import java.util.Map;

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
    private int rate;
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

    /**
     * Extends a player object with information retrieved from a RCON call to
     * the status command
     *
     * @param playerData The player data retrieved from
     *        <code>rcon status</code>
     * @throws SteamCondenserException
     */
    public void addInformation(Map<String, String> playerData)
            throws SteamCondenserException {
        if(!playerData.get("name").equals(this.name)) {
            throw new SteamCondenserException("Information to add belongs to a different player.");
        }

        this.extended = true;
        this.realId = Integer.parseInt(playerData.get("userid"));
        this.steamId = playerData.get("uniqueid");

        if(playerData.containsKey("state")) {
            this.state = playerData.get("state");
        }

        if(!this.isBot()) {
            this.loss  = Integer.parseInt(playerData.get("loss"));
            this.ping  = Integer.parseInt(playerData.get("ping"));

            if(playerData.containsKey("adr")) {
                String[] address = playerData.get("adr").split(":");
                this.ipAddress   = address[0];
                this.clientPort  = Integer.parseInt(address[1]);
            }

            if(playerData.containsKey("rate")) {
                this.rate = Integer.parseInt(playerData.get("rate"));
            }
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
     * @return Returns the rate of this player
     */
    public int getRate() {
        return this.rate;
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
     * Returns whether this player is a bot
     */
    public boolean isBot() {
        return this.steamId.equals("BOT");
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
