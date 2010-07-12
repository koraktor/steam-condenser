/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.css;

import org.w3c.dom.Element;

/**
 * CSSMap holds statistical information about maps played by a player in
 * Counter-Strike: Source.
 *
 * @author Sebastian Staudt
 */
public class CSSMap {

    private boolean favorite;

    private String name;

    private int roundsPlayed;

    private int roundsLost;

    private int roundsWon;

    private float roundsWonPercentage;

    /**
     * Creates a new instance of CSSMap based on the assigned map name and XML
     * data
     *
     * @param mapName
     * @param mapsData
     */
    public CSSMap(String mapName, Element mapsData) {
        this.name = mapName;

        this.favorite     = mapsData.getElementsByTagName("favorite").item(0).getTextContent().equals(this.name);
        this.roundsPlayed = Integer.parseInt(mapsData.getElementsByTagName(this.name + "_rounds").item(0).getTextContent());
        this.roundsWon    = Integer.parseInt(mapsData.getElementsByTagName(this.name + "_wins").item(0).getTextContent());

        this.roundsLost          = this.roundsPlayed - this.roundsWon;
        this.roundsWonPercentage = (this.roundsPlayed > 0) ? (this.roundsWon / this.roundsPlayed) : 0;
    }

    /**
     * Returns whether this map is the favorite map of this player
     *
     * @return true if this is the favorite map
     */
    public boolean isFavorite() {
        return this.favorite;
    }

    public String getName() {
        return this.name;
    }

    public int getRoundsPlayed() {
        return this.roundsPlayed;
    }

    public int getRoundsLost() {
        return this.roundsLost;
    }

    public int getRoundsWon() {
        return this.roundsWon;
    }

    public float getRoundsWonPercentage() {
        return this.roundsWonPercentage;
    }
}
