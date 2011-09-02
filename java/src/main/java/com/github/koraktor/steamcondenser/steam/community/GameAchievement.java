/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.github.koraktor.steamcondenser.exceptions.WebApiException;

/**
 * The GameAchievement class represents a specific achievement for a single
 * game and for a single user
 * <p>
 * It also provides the ability to load the global unlock percentages of all
 * achievements of a specific game.
 *
 * @author Sebastian Staudt
 */
public class GameAchievement {

    private String apiName;

    private int appId;

    private String description;

    private String name;

    private long steamId64;

    private Date timestamp;

    private boolean unlocked;

    /**
     * Loads the global unlock percentages of all achievements for the given
     * game
     *
     * @param appId The unique Steam Application ID of the game (e.g.
     *        <code>440</code> for Team Fortress 2). See
     *        http://developer.valvesoftware.com/wiki/Steam_Application_IDs for
     *        all application IDs
     * @return The symbolic achievement names with the corresponding global
     *         unlock percentages
     * @throws WebApiException if a request to Steam's Web API fails
     */
    public static Map<String, Double> getGlobalPercentages(int appId)
            throws WebApiException {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("gameid", appId);

        try {
            JSONObject data = new JSONObject(WebApi.getJSON("ISteamUserStats", "GetGlobalAchievementPercentagesForApp", 2, params));

            HashMap<String, Double> percentages = new HashMap<String, Double>();
            JSONArray achievementsData = data.getJSONObject("achievementpercentages").getJSONArray("achievements");
            for(int i = 0; i < achievementsData.length(); i ++) {
                JSONObject achievementData = achievementsData.getJSONObject(i);
                percentages.put(achievementData.getString("name"), achievementData.getDouble("percent"));
            }

            return percentages;
        } catch(JSONException e) {
            throw new WebApiException("Could not parse JSON data.", e);
        }
    }

    /**
     * Creates the achievement with the given name for the given user and game
     * and achievement data
     *
     * @param steamId64 The 64bit SteamID of the player this achievement
     *        belongs to
     * @param appId The unique Steam Application ID of the game (e.g.
     *        <code>440</code> for Team Fortress 2). See
     *        http://developer.valvesoftware.com/wiki/Steam_Application_IDs for
     *        all application IDs
     * @param achievementData The achievement data extracted from XML
     */
    public GameAchievement(long steamId64, int appId, Element achievementData) {
        this.apiName     = achievementData.getElementsByTagName("apiname").item(0).getTextContent();
        this.appId       = appId;
        this.description = achievementData.getElementsByTagName("description").item(0).getTextContent();
        this.name        = achievementData.getElementsByTagName("name").item(0).getTextContent();
        this.steamId64   = steamId64;
        this.unlocked    = achievementData.getAttribute("closed").equals("1");

        NodeList unlockTimestampElements = achievementData.getElementsByTagName("unlockTimestamp");
        if(this.unlocked && unlockTimestampElements.getLength() != 0) {
            this.timestamp = new Date(Long.parseLong(achievementData.getElementsByTagName("unlockTimestamp").item(0).getTextContent()) * 1000);
        }
    }

    /**
     * Returns the symbolic API name of this achievement
     *
     * @return The API name of this achievement
     */
    public String getApiName() {
        return this.apiName;
    }

    /**
     * Return the unique Steam Application ID of the game this achievement
     * belongs to
     *
     * @return The Steam Application ID of this achievement's game
     */
    public int getAppId() {
        return this.appId;
    }

    /**
     * Return the description of this achievement
     *
     * @return The description of this achievement
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the name of this achievement
     *
     * @return The name of this achievement
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the 64bit SteamID of the user who owns this achievement
     *
     * @return The 64bit SteamID of this achievement's owner
     */
    public long getSteamId64() {
        return this.steamId64;
    }

    /**
     * Returns the time this achievement has been unlocked by its owner
     *
     * @return The time this achievement has been unlocked
     */
    public Date getTimestamp() {
        return this.timestamp;
    }

    /**
     * Returns whether this achievement has been unlocked by its owner
     *
     * @return <code>true</code> if the achievement has been unlocked by
     *         the user
     */
    public boolean isUnlocked() {
        return this.unlocked;
    }
}
