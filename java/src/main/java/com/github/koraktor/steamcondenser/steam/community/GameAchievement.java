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
 * The GameAchievement class represents a specific achievement for a single game
 * and for a single user
 *
 * @author Sebastian Staudt
 */
public class GameAchievement
{
    private int appId;

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
     * @throws JSONException If the JSON data cannot be parsed
     * @throws WebApiException If a request to Steam's Web API fails
     */
    public static Map<String, Double> getGlobalPercentages(int appId)
            throws JSONException, WebApiException {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("gameid", appId);
        JSONObject data = new JSONObject(WebApi.getJSON("ISteamUserStats", "GetGlobalAchievementPercentagesForApp", 2, params));

        HashMap<String, Double> percentages = new HashMap<String, Double>();
        JSONArray achievementsData = data.getJSONObject("achievementpercentages").getJSONArray("achievements");
        for(int i = 0; i < achievementsData.length(); i ++) {
            JSONObject achievementData = achievementsData.getJSONObject(i);
            percentages.put(achievementData.getString("name"), achievementData.getDouble("percent"));
        }

        return percentages;
    }

    /**
     * Creates the achievement with the given name for the given user and game
     * and achievement data
     *
     * @param steamId64 The 64bit SteamID of the player
     * @param appId The AppID of the game this achievement belongs to
     * @param achievementData The XML data for this achievement
     */
    public GameAchievement(long steamId64, int appId, Element achievementData)
    {
        this.appId     = appId;
        this.name      = achievementData.getElementsByTagName("name").item(0).getTextContent();
        this.steamId64 = steamId64;
        this.unlocked  = achievementData.getAttribute("closed").equals("1");

        NodeList unlockTimestampElements = achievementData.getElementsByTagName("unlockTimestamp");
        if(this.unlocked && unlockTimestampElements.getLength() != 0) {
            this.timestamp = new Date(Long.parseLong(achievementData.getElementsByTagName("unlockTimestamp").item(0).getTextContent()) * 1000);
        }
    }

    /**
     * Returns the AppID of the game this achievements belongs to
     *
     * @return The AppID of the game
     */
    public int getAppId(){
        return this.appId;
    }

    /**
     * Returns the name of this achievement
     *
     * @return The name of the achievement
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the 64bit SteamID of the player this achievement belongs to
     *
     * @return The 64bit SteamID of the player
     */
    public long getSteamId64() {
        return this.steamId64;
    }

    /**
     * Returns the timestamp at which this achievement has been unlocked
     *
     * @return The unlock timestamp
     */
    public Date getTimestamp() {
        return this.timestamp;
    }

    /**
     * Returns whether this achievement has been unlocked by its owner
     *
     * @return If this achievement has been unlocked
     */
    public boolean isUnlocked() {
        return this.unlocked;
    }
}
