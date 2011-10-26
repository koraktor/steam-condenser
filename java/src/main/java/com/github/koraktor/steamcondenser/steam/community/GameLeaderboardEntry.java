/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community;

import org.w3c.dom.Element;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;

/**
 * The GameLeaderboard class represents a single entry in a leaderboard
 *
 * @author Sebastian Staudt
 */
public class GameLeaderboardEntry {

    protected SteamId steamId;

    protected int score;

    protected int rank;

    protected GameLeaderboard leaderboard;

    /**
     * Creates new entry instance for the given XML data and leaderboard
     *
     * @param entryData The XML data of the leaderboard of the leaderboard
     *        entry
     * @param leaderboard The leaderboard this entry belongs to
     */
    public GameLeaderboardEntry(Element entryData, GameLeaderboard leaderboard) {
        try {
            this.steamId     = SteamId.create(entryData.getElementsByTagName("steamid").item(0).getTextContent(), false);
        } catch(SteamCondenserException e) {}
        this.score       = Integer.parseInt(entryData.getElementsByTagName("score").item(0).getTextContent());
        this.rank        = Integer.parseInt(entryData.getElementsByTagName("rank").item(0).getTextContent());
        this.leaderboard = leaderboard;
    }

    /**
     * Returns the Steam ID of this entry's player
     *
     * @return The Steam ID of the player
     */
    public SteamId getSteamId() {
        return this.steamId;
    }

    /**
     * Returns the score of this entry
     *
     * @return The score of this player
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Returns the rank where this entry is listed in the leaderboard
     *
     * @return The rank of this entry
     */
    public int getRank() {
        return this.rank;
    }

    /**
     * Returns the leaderboard this entry belongs to
     *
     * @return The leaderboard of this entry
     */
    public GameLeaderboard getLeaderboard() {
        return this.leaderboard;
    }

}
