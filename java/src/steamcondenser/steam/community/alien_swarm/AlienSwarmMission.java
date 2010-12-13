/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010, Sebastian Staudt
 */

package steamcondenser.steam.community.alien_swarm;

import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Element;

/**
 * AlienSwarmMission holds statistical information about missions played by a
 * player in Alien Swarm.
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
     * Creates a new instance of AlienSwarmMission based on the assigned
     * mission name and XML data
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

    public float getAvgDamageTaken() {
        return this.avgDamageTaken;
    }

    public float getAvgFriendlyFire() {
        return this.avgFriendlyFire;
    }

    public float getAvgKills() {
        return this.avgKills;
    }

    public String getBestDifficulty() {
        return this.bestDifficulty;
    }

    public int getDamageTaken() {
        return this.damageTaken;
    }

    public int getFriendlyFire() {
        return this.friendlyFire;
    }

    public int getGamesSuccessful() {
        return this.gamesSuccessful;
    }

    public String getImg() {
        return this.img;
    }

    public int getKills() {
        return this.kills;
    }

    public String getMapName() {
        return this.mapName;
    }

    public String getName() {
        return this.name;
    }

    public Map<String, String> getTime() {
        return this.time;
    }

    public int getTotalGames() {
        return this.totalGames;
    }

    public float getTotalGamesPercentage() {
        return this.totalGamesPercentage;
    }

}
