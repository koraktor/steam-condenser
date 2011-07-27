/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.l4d;

import org.w3c.dom.Element;

/**
 * This class holds statistical information about a map played by a player in
 * Survival mode of Left4Dead
 *
 * @author Sebastian Staudt
 */
public class L4DMap {
    public static int GOLD   = 1;
    public static int SILVER = 2;
    public static int BRONZE = 3;
    public static int NONE   = 0;

    protected float bestTime;

    protected String id;

    protected int medal;

    protected String name;

    private int timesPlayed;

    public L4DMap() {}

    /**
     * Creates a new instance of a Left4Dead Survival map based on the given
     * XML data
     *
     * @param mapData The XML data for this map
     */
    public L4DMap(Element mapData) {
        this.bestTime    = Float.parseFloat(mapData.getElementsByTagName("besttimeseconds").item(0).getTextContent());
        this.id          = mapData.getNodeName();
        this.name        = mapData.getElementsByTagName("name").item(0).getTextContent();
        this.timesPlayed = Integer.parseInt(mapData.getElementsByTagName("timesplayed").item(0).getTextContent());

        String medal = mapData.getElementsByTagName("medal").item(0).getTextContent();
        if(medal.equals("gold")) {
            this.medal = GOLD;
        } else if(medal.equals("silver")) {
            this.medal = SILVER;
        } else if(medal.equals("bronze")) {
            this.medal = BRONZE;
        } else {
            this.medal = NONE;
        }
    }

    /**
     * Returns the best survival time of this player on this map
     *
     * @return The best survival time of this player on this map
     */
    public float getBestTime() {
        return this.bestTime;
    }

    /**
     * Returns the ID of this map
     *
     * @return The ID of this map
     */
    public String getId() {
        return this.id;
    }

    /**
     * Returns the highest medal this player has won on this map
     *
     * @return The highest medal won by this player on this map
     */
    public int getMedal() {
        return this.medal;
    }

    /**
     * Returns the name of the map
     *
     * @return The name of the map
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the number of times this map has been played by this player
     *
     * @return The number of times this map has been played
     */
    public int getTimesPlayed() {
        return this.timesPlayed;
    }
}
