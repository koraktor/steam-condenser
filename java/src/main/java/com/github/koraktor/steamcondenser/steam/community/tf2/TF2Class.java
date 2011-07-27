/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.tf2;

import org.w3c.dom.Element;
import com.github.koraktor.steamcondenser.steam.community.GameClass;

/**
 * Represents the stats for a Team Fortress 2 class for a specific user
 *
 * @author Sebastian Staudt
 */
public class TF2Class extends GameClass {

    protected int maxBuildingsDestroyed;
    protected int maxCaptures;
    protected int maxDamage;
    protected int maxDefenses;
    protected int maxDominations;
    protected int maxKillAssists;
    protected int maxKills;
    protected int maxRevenges;
    protected int maxScore;
    protected int maxTimeAlive;

    /**
     * Creates a new TF2 class instance based on the assigned XML data
     *
     * @param classData The XML data for this class
     */
    public TF2Class(Element classData) {
        this.name                  = classData.getElementsByTagName("className").item(0).getTextContent();
        this.maxBuildingsDestroyed = Integer.parseInt(classData.getElementsByTagName("ibuildingsdestroyed").item(0).getTextContent());
        this.maxCaptures           = Integer.parseInt(classData.getElementsByTagName("ipointcaptures").item(0).getTextContent());
        this.maxDamage             = Integer.parseInt(classData.getElementsByTagName("idamagedealt").item(0).getTextContent());
        this.maxDefenses           = Integer.parseInt(classData.getElementsByTagName("ipointdefenses").item(0).getTextContent());
        this.maxDominations        = Integer.parseInt(classData.getElementsByTagName("idominations").item(0).getTextContent());
        this.maxKillAssists        = Integer.parseInt(classData.getElementsByTagName("ikillassists").item(0).getTextContent());
        this.maxKills              = Integer.parseInt(classData.getElementsByTagName("inumberofkills").item(0).getTextContent());
        this.maxRevenges           = Integer.parseInt(classData.getElementsByTagName("irevenge").item(0).getTextContent());
        this.maxScore              = Integer.parseInt(classData.getElementsByTagName("ipointsscored").item(0).getTextContent());
        this.maxTimeAlive          = Integer.parseInt(classData.getElementsByTagName("iplaytime").item(0).getTextContent());
        this.playTime              = Integer.parseInt(classData.getElementsByTagName("playtimeSeconds").item(0).getTextContent());
    }

    /**
     * Returns the maximum number of buildings the player has destroyed in a
     * single life with this class
     *
     * @return Maximum number of buildings destroyed
     */
    public int getMaxBuildingsDestroyed() {
        return this.maxBuildingsDestroyed;
    }

    /**
     * Returns the maximum number of points captured by the player in a single
     * life with this class
     *
     * @return Maximum number of points captured
     */
    public int getMaxCaptures() {
        return this.maxCaptures;
    }

    /**
     * Returns the maximum damage dealt by the player in a single life with
     * this class
     *
     * @return Maximum damage dealt
     */
    public int getMaxDamage() {
        return this.maxDamage;
    }

    /**
     * Returns the maximum number of defenses by the player in a single life
     * with this class
     *
     * @return Maximum number of defenses
     */
    public int getMaxDefenses() {
        return this.maxDefenses;
    }

    /**
     * Returns the maximum number of dominations by the player in a single life
     * with this class
     *
     * @return Maximum number of dominations
     */
    public int getMaxDominations() {
        return this.maxDominations;
    }

    /**
     * Returns the maximum number of times the the player assisted a teammate
     * with killing an enemy in a single life with this class
     *
     * @return Maximum number of kill assists
     */
    public int getMaxKillAssists() {
        return this.maxKillAssists;
    }

    /**
     * Returns the maximum number of enemies killed by the player in a single
     * life with this class
     *
     * @return Maximum number of kills
     */
    public int getMaxKills() {
        return this.maxKills;
    }

    /**
     * Returns the maximum number of revenges by the player in a single life
     * with this class
     *
     * @return Maximum number of revenges
     */
    public int getMaxRevenges() {
        return this.maxRevenges;
    }

    /**
     * Returns the maximum number score achieved by the player in a single life
     * with this class
     *
     * @return Maximum score
     */
    public int getMaxScore() {
        return this.maxScore;
    }

    /**
     * Returns the maximum lifetime by the player in a single life with this
     * class
     *
     * @return Maximum lifetime
     */
    public int getMaxTimeAlive() {
        return this.maxTimeAlive;
    }
}
