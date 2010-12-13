/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2010, Sebastian Staudt
 */

package steamcondenser.steam.community;

import java.util.Date;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
