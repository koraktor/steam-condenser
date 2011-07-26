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
 * This class represents a player connected to a game server
 *
 * @author  Sebastian Staudt
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
     * Creates a new player instancewith the given information
     *
     * @param int id The ID of the player on the server
     * @param string name The name of the player
     * @param int score The score of the player
     * @param float connectTime The time the player is connected to the server
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
     * @param playerData The player data retrieved from <code>rcon
     *        status</code>
     * @throws SteamCondenserException if the information belongs to another
     *         player
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
     *
     * @return The client port of the player
     */
    public int getClientPort()
    {
        return this.clientPort;
    }

    /**
     * Returns the time this player is connected to the server
     *
     * @return The connection time of the player
     */
    public float getConnectTime() {
        return this.connectTime;
    }

    /**
     * Returns the ID of this player
     *
     * @return The ID of this player
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the IP address of this player
     *
     * @return The IP address of this player
     */
    public String getIpAddress() {
        return this.ipAddress;
    }

    /**
     * Returns the packet loss of this player's connection
     *
     * @return The packet loss of this player's connection
     */
    public int getLoss() {
        return this.loss;
    }

    /**
     * Returns the nickname of this player
     *
     * @return The name of this player
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the ping of this player
     *
     * @return The ping of this player
     */
    public int getPing() {
        return this.ping;
    }

    /**
     * Returns the rate of this player
     *
     * @return The rate of this player
     */
    public int getRate() {
        return this.rate;
    }

    /**
     * Returns the real ID (as used on the server) of this player
     *
     * @return The real ID of this player
     */
    public int getRealId()
    {
        return this.realId;
    }

    /**
     * Returns the score of this player
     *
     * @return The score of this player
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Returns the connection state of this player
     *
     * @return The connection state of this player
     */
    public String getState() {
        return this.state;
    }

    /**
     * Returns the SteamID of this player
     *
     * @return The SteamID of this player
     */
    public String getSteamId()
    {
        return this.steamId;
    }

    /**
     * Returns whether this player is a bot
     *
     * @return <code>true</code> if this player is a bot
     */
    public boolean isBot() {
        return this.steamId.equals("BOT");
    }

    /**
     * Returns whether this player object has extended information gathered
     * using RCON
     *
     * @return <code>true</code> if extended information for this player is
     *         available
     */
    public boolean isExtended()
    {
        return this.extended;
    }

    /**
     * Returns a string representation of this player
     *
     * @return A string representing this player
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
