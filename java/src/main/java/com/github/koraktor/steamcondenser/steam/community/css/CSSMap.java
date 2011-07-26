/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.css;

import org.w3c.dom.Element;

/**
 * Represents the stats for a Counter-Strike: Source map for a specific user
 *
 * @author Sebastian Staudt
 */
public class CSSMap {

    private boolean favorite;

    private String name;

    private int roundsPlayed;

    private int roundsLost;

    private int roundsWon;

    /**
     * Creates a new instance of a Counter-Strike: Source class based on the
     * given XML data
     *
     * @param mapName The name of the map
     * @param mapsData The XML data of all maps
     */
    public CSSMap(String mapName, Element mapsData) {
        this.name = mapName;

        this.favorite     = mapsData.getElementsByTagName("favorite").item(0).getTextContent().equals(this.name);
        this.roundsPlayed = Integer.parseInt(mapsData.getElementsByTagName(this.name + "_rounds").item(0).getTextContent());
        this.roundsWon    = Integer.parseInt(mapsData.getElementsByTagName(this.name + "_wins").item(0).getTextContent());
        this.roundsLost   = this.roundsPlayed - this.roundsWon;
    }

    /**
     * Returns whether this map is the favorite map of this player
     *
     * @return <code>true</code> if this is the favorite map
     */
    public boolean isFavorite() {
        return this.favorite;
    }

    /**
     * Returns the name of this map
     *
     * @return The name of this map
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the number of rounds the player has lost on this map
     *
     * @return The number of rounds lost
     */
    public int getRoundsLost() {
        return this.roundsLost;
    }

    /**
     * Returns the number of rounds the player has played on this map
     *
     * @return The number of rounds played
     */
    public int getRoundsPlayed() {
        return this.roundsPlayed;
    }

    /**
     * Returns the number of rounds the player has won on this map
     *
     * @return The number of rounds won
     */
    public int getRoundsWon() {
        return this.roundsWon;
    }

    /**
     * Returns the percentage of rounds the player has won on this map
     *
     * @return The percentage of rounds won
     */
    public float getRoundsWonPercentage() {
        return (this.roundsPlayed > 0) ? (this.roundsWon / this.roundsPlayed) : 0;
    }
}
