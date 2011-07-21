/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.alien_swarm;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

/**
 * This class holds statistical information about missions played by a player
 * in Alien Swarm
 *
 * @author Sebastian Staudt
 */
public class AlienSwarmMission {

    private float avgDamageTaken;

    private float avgFriendlyFire;

    private float avgKills;

    private String bestDifficulty;

    private int damageTaken;

    private int friendlyFire;

    private int gamesSuccessful;

    private String img;

    private int kills;

    private String mapName;

    private String name;

    private Map<String, String> time;

    private int totalGames;

    private float totalGamesPercentage;

    /**
     * Creates a new mission instance of based on the given XML data
     *
     * @param missionData The data representing this mission
     */
    public AlienSwarmMission(Element missionData) {
        this.avgDamageTaken       = Float.valueOf(missionData.getElementsByTagName("damagetakenavg").item(0).getTextContent());
        this.avgFriendlyFire      = Float.valueOf(missionData.getElementsByTagName("friendlyfireavg").item(0).getTextContent());
        this.avgKills             = Float.valueOf(missionData.getElementsByTagName("killsavg").item(0).getTextContent());
        this.bestDifficulty       = missionData.getElementsByTagName("bestdifficulty").item(0).getTextContent();
        this.damageTaken          = Integer.valueOf(missionData.getElementsByTagName("damagetaken").item(0).getTextContent());
        this.friendlyFire         = Integer.valueOf(missionData.getElementsByTagName("friendlyfire").item(0).getTextContent());
        this.gamesSuccessful      = Integer.valueOf(missionData.getElementsByTagName("gamessuccess").item(0).getTextContent());
        this.img                  = AlienSwarmStats.BASE_URL + missionData.getElementsByTagName("image").item(0).getTextContent();
        this.kills                = Integer.valueOf(missionData.getElementsByTagName("kills").item(0).getTextContent());
        this.mapName              = missionData.getNodeName();
        this.name                 = missionData.getElementsByTagName("name").item(0).getTextContent();
        this.totalGames           = Integer.valueOf(missionData.getElementsByTagName("gamestotal").item(0).getTextContent());
        this.totalGamesPercentage = Float.valueOf(missionData.getElementsByTagName("gamestotalpct").item(0).getTextContent());

        this.time = new HashMap<String, String>();
        this.time.put("average", missionData.getElementsByTagName("avgtime").item(0).getTextContent());
        this.time.put("brutal", missionData.getElementsByTagName("brutaltime").item(0).getTextContent());
        this.time.put("easy", missionData.getElementsByTagName("easytime").item(0).getTextContent());
        this.time.put("hard", missionData.getElementsByTagName("hardtime").item(0).getTextContent());
        this.time.put("insane", missionData.getElementsByTagName("insanetime").item(0).getTextContent());
        this.time.put("normal", missionData.getElementsByTagName("normaltime").item(0).getTextContent());
        this.time.put("total", missionData.getElementsByTagName("totaltime").item(0).getTextContent());
    }

    /**
     * Returns the avarage damage taken by the player while playing a round in
     * this mission
     *
     * @return The average damage taken by the player
     */
    public float getAvgDamageTaken() {
        return this.avgDamageTaken;
    }

    /**
     * Returns the avarage damage dealt by the player to team mates while
     * playing a round in this mission
     *
     * @return The average damage dealt by the player to team mates
     */
    public float getAvgFriendlyFire() {
        return this.avgFriendlyFire;
    }

    /**
     * Returns the avarage number of aliens killed by the player while playing
     * a round in this mission
     *
     * @return The avarage number of aliens killed by the player
     */
    public float getAvgKills() {
        return this.avgKills;
    }

    /**
     * Returns the highest difficulty the player has beat this mission in
     *
     * @return The highest difficulty the player has beat this mission in
     */
    public String getBestDifficulty() {
        return this.bestDifficulty;
    }

    /**
     * Returns the total damage taken by the player in this mission
     *
     * @return The total damage taken by the player
     */
    public int getDamageTaken() {
        return this.damageTaken;
    }

    /**
     * Returns the total damage dealt by the player to team mates in this
     * mission
     *
     * @return The total damage dealt by the player to team mates
     */
    public int getFriendlyFire() {
        return this.friendlyFire;
    }

    /**
     * Returns the number of successful rounds the player played in this
     * mission
     *
     * @return The number of successful rounds of this mission
     */
    public int getGamesSuccessful() {
        return this.gamesSuccessful;
    }

    /**
     * Returns the URL to a image displaying the mission
     *
     * @return The URL of the mission's image
     */
    public String getImg() {
        return this.img;
    }

    /**
     * Returns the total number of aliens killed by the player in this mission
     *
     * @return The total number of aliens killed by the player
     */
    public int getKills() {
        return this.kills;
    }

    /**
     * Returns the file name of the mission's map
     *
     * @return The file name of the mission's map
     */
    public String getMapName() {
        return this.mapName;
    }

    /**
     * Returns the name of the mission
     *
     * @return The name of the mission
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns various statistics about the times needed to accomplish this
     * mission
     *
     * This includes the best times for each difficulty, the average time and
     * the total time spent in this mission.
     *
     * @return Various time statistics about this mission
     */
    public Map<String, String> getTime() {
        return this.time;
    }

    /**
     * Returns the number of games played in this mission
     *
     * @return The number of games played in this mission
     */
    public int getTotalGames() {
        return this.totalGames;
    }

    /**
     * Returns the percentage of successful games played in this mission
     *
     * @return The percentage of successful games played in this mission
     */
    public float getTotalGamesPercentage() {
        return this.totalGamesPercentage;
    }

}
