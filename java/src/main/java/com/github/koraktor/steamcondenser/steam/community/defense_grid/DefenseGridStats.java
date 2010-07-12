/*
 * This code is free software; you can redistribute it and/or modify it under the
 * terms of the new BSD License.
 *
 * Copyright (c) 2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.defense_grid;

import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Element;
import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.GameStats;

/**
 *
 * @author koraktor
 */
public class DefenseGridStats extends GameStats {
    private HashMap<String, int[]> alienStats;
    private int bronzeMedals;
    private int silverMedals;
    private int goldMedals;
    private int levelsPlayed;
    private int levelsPlayedCampaign;
    private int levelsPlayedChallenge;
    private int levelsWon;
    private int levelsWonCampaign;
    private int levelsWonChallenge;
    private int encountered;
    private int killed;
    private int killedCampaign;
    private int killedChallenge;
    private float resources;
    private float heatDamage;
    private float timePlayed;
    private float interest;
    private float damage;
    private float damageCampaign;
    private float damageChallenge;
    private int orbitalLaserFired;
    private float orbitalLaserDamage;
    private HashMap<String, ArrayList<float[]>> towerStats;

    public DefenseGridStats(Object steamId)
            throws SteamCondenserException {
        super(steamId, "defensegrid:awakening");

        if(this.isPublic()) {
            XPath xpath = XPathFactory.newInstance().newXPath();

            try {
                Element generalData = (Element) xpath.evaluate("stats/general", this.xmlData, XPathConstants.NODE);
                this.bronzeMedals = Integer.parseInt(((Element) xpath.evaluate("bronze_medals_won/value", generalData, XPathConstants.NODE)).getTextContent());
                this.silverMedals = Integer.parseInt(((Element) xpath.evaluate("silver_medals_won/value", generalData, XPathConstants.NODE)).getTextContent());
                this.goldMedals = Integer.parseInt(((Element) xpath.evaluate("gold_medals_won/value", generalData, XPathConstants.NODE)).getTextContent());
                this.levelsPlayed = Integer.parseInt(((Element) xpath.evaluate("levels_played_total/value", generalData, XPathConstants.NODE)).getTextContent());
                this.levelsPlayedCampaign = Integer.parseInt(((Element) xpath.evaluate("levels_played_campaign/value", generalData, XPathConstants.NODE)).getTextContent());
                this.levelsPlayedChallenge = Integer.parseInt(((Element) xpath.evaluate("levels_played_challenge/value", generalData, XPathConstants.NODE)).getTextContent());
                this.levelsWon = Integer.parseInt(((Element) xpath.evaluate("levels_won_total/value", generalData, XPathConstants.NODE)).getTextContent());
                this.levelsWonCampaign = Integer.parseInt(((Element) xpath.evaluate("levels_won_campaign/value", generalData, XPathConstants.NODE)).getTextContent());
                this.levelsWonChallenge = Integer.parseInt(((Element) xpath.evaluate("levels_won_challenge/value", generalData, XPathConstants.NODE)).getTextContent());
                this.encountered = Integer.parseInt(((Element) xpath.evaluate("total_aliens_encountered/value", generalData, XPathConstants.NODE)).getTextContent());
                this.killed = Integer.parseInt(((Element) xpath.evaluate("total_aliens_killed/value", generalData, XPathConstants.NODE)).getTextContent());
                this.killedCampaign = Integer.parseInt(((Element) xpath.evaluate("total_aliens_killed_campaign/value", generalData, XPathConstants.NODE)).getTextContent());
                this.killedChallenge = Integer.parseInt(((Element) xpath.evaluate("total_aliens_killed_challenge/value", generalData, XPathConstants.NODE)).getTextContent());
                this.resources = Float.parseFloat(((Element) xpath.evaluate("resources_recovered/value", generalData, XPathConstants.NODE)).getTextContent());
                this.heatDamage = Float.parseFloat(((Element) xpath.evaluate("heatdamage/value", generalData, XPathConstants.NODE)).getTextContent());
                this.timePlayed = Float.parseFloat(((Element) xpath.evaluate("time_played/value", generalData, XPathConstants.NODE)).getTextContent());
                this.interest = Float.parseFloat(((Element) xpath.evaluate("interest_gained/value", generalData, XPathConstants.NODE)).getTextContent());
                this.damage = Float.parseFloat(((Element) xpath.evaluate("tower_damage_total/value", generalData, XPathConstants.NODE)).getTextContent());
                this.damageCampaign = Float.parseFloat(((Element) xpath.evaluate("tower_damage_total_campaign/value", generalData, XPathConstants.NODE)).getTextContent());
                this.damageChallenge = Float.parseFloat(((Element) xpath.evaluate("tower_damage_total_challenge/value", generalData, XPathConstants.NODE)).getTextContent());
                this.orbitalLaserFired = Integer.parseInt(((Element) xpath.evaluate("stats/orbitallaser/fired/value", this.xmlData, XPathConstants.NODE)).getTextContent());
                this.orbitalLaserDamage = Float.parseFloat(((Element) xpath.evaluate("stats/orbitallaser/damage/value", this.xmlData, XPathConstants.NODE)).getTextContent());
            } catch(XPathExpressionException e) {
                throw new SteamCondenserException("Stats could not be parsed.");
            }
        }
    }

    public HashMap<String, int[]> getAlienStats()
            throws SteamCondenserException {
        if(!this.isPublic()) {
            return null;
        }

        if(this.alienStats != null) {
            try {
                XPath xpath = XPathFactory.newInstance().newXPath();
                Element aliensData = (Element) xpath.evaluate("stats/aliens", this.xmlData, XPathConstants.NODE);
                this.alienStats = new HashMap<String, int[]>();
                String[] aliens = {"bulwark", "crasher", "dart", "decoy",
                    "drone", "grunt", "juggernaut", "manta", "racer", "rumbler",
                    "seeker", "spire", "stealth", "swarmer", "turtle", "walker"};

                for(String alien : aliens) {
                    int[] alienData = new int[2];
                    alienData[0] = Integer.parseInt(((Element) xpath.evaluate(alien + "/encountered/value", aliensData, XPathConstants.NODE)).getTextContent());
                    alienData[1] = Integer.parseInt(((Element) xpath.evaluate(alien + "/killed/value", aliensData, XPathConstants.NODE)).getTextContent());
                    this.alienStats.put(alien, alienData);
                }
            } catch(XPathExpressionException e) {
                throw new SteamCondenserException("Stats could not be parsed.");
            }
        }

        return this.alienStats;
    }

    public int getBronzeMedals() {
        return bronzeMedals;
    }

    public float getDamage() {
        return damage;
    }

    public float getDamageCampaign() {
        return damageCampaign;
    }

    public float getDamageChallenge() {
        return damageChallenge;
    }

    public int getEncountered() {
        return encountered;
    }

    public int getGoldMedals() {
        return goldMedals;
    }

    public float getHeatDamage() {
        return heatDamage;
    }

    public float getInterest() {
        return interest;
    }

    public int getKilled() {
        return killed;
    }

    public int getKilledCampaign() {
        return killedCampaign;
    }

    public int getKilledChallenge() {
        return killedChallenge;
    }

    public int getLevelsPlayed() {
        return levelsPlayed;
    }

    public int getLevelsPlayedCampaign() {
        return levelsPlayedCampaign;
    }

    public int getLevelsPlayedChallenge() {
        return levelsPlayedChallenge;
    }

    public int getLevelsWon() {
        return levelsWon;
    }

    public int getLevelsWonCampaign() {
        return levelsWonCampaign;
    }

    public int getLevelsWonChallenge() {
        return levelsWonChallenge;
    }

    public float getOrbitalLaserDamage() {
        return orbitalLaserDamage;
    }

    public int getOrbitalLaserFired() {
        return orbitalLaserFired;
    }

    public float getResources() {
        return resources;
    }

    public int getSilverMedals() {
        return silverMedals;
    }

    public HashMap<String, ArrayList<float[]>> getTowerStats()
            throws SteamCondenserException {
        if(!this.isPublic()) {
            return null;
        }

        if(this.towerStats != null) {
            try {
                XPath xpath = XPathFactory.newInstance().newXPath();
                Element towersData = (Element) xpath.evaluate("stats/towers", this.xmlData, XPathConstants.NODE);
                this.towerStats = new HashMap<String, ArrayList<float[]>>();
                String[] towers = {"cannon", "flak", "gun", "inferno", "laser", "meteor", "missile", "tesla"};

                ArrayList<float[]> towerData;
                for(String tower : towers) {
                    towerData = new ArrayList<float[]>();
                    for(int i = 1; i <= 3; i++) {
                        float[] levelData = new float[2];
                        levelData[0] = Float.parseFloat(((Element) xpath.evaluate(tower + "[@level=" + i + "/built/value", towersData, XPathConstants.NODE)).getTextContent());
                        levelData[1] = Float.parseFloat(((Element) xpath.evaluate(tower + "[@level=" + i + "/damage/value", towersData, XPathConstants.NODE)).getTextContent());
                        towerData.add(i, levelData);
                    }
                    this.towerStats.put(tower, towerData);
                }

                towerData = new ArrayList<float[]>();
                for(int i = 1; i <= 3; i++) {
                    float[] levelData = new float[2];
                    levelData[0] = Float.parseFloat(((Element) xpath.evaluate("command[@level=" + i + "/built/value", towersData, XPathConstants.NODE)).getTextContent());
                    levelData[1] = Float.parseFloat(((Element) xpath.evaluate("command[@level=" + i + "/resource/value", towersData, XPathConstants.NODE)).getTextContent());
                    towerData.add(i, levelData);
                }
                this.towerStats.put("command", towerData);

                towerData = new ArrayList<float[]>();
                for(int i = 1; i <= 3; i++) {
                    float[] levelData = new float[2];
                    levelData[0] = Float.parseFloat(((Element) xpath.evaluate("temporal[@level=" + i + "/built/value", towersData, XPathConstants.NODE)).getTextContent());
                    towerData.add(i, levelData);
                }
                this.towerStats.put("temporal", towerData);
            } catch(XPathExpressionException e) {
                throw new SteamCondenserException("Stats could not be parsed.");
            }
        }

        return this.towerStats;
    }

    public float getTimePlayed() {
        return timePlayed;
    }

}
