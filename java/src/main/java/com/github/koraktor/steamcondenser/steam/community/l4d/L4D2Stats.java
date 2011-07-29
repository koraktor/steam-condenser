/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.l4d;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.GameWeapon;

/**
 * This class represents the game statistics for a single user in Left4Dead 2
 *
 * @author Sebastian Staudt
 */
public class L4D2Stats extends AbstractL4DStats {

    private HashMap<String, Object> scavengeStats;

    /**
     * Creates a <code>L4D2Stats</code> object by calling the super constructor
     * with the game name <code>"l4d2"</code>
     *
     * @param steamId The custom URL or 64bit Steam ID of the user
     */
    public L4D2Stats(Object steamId)
            throws SteamCondenserException {
        super(steamId, "l4d2");
    }

    /**
     * Returns a map of lifetime statistics for this user like the time played
     * <p>
     * If the lifetime statistics haven't been parsed already, parsing is done
     * now.
     * <p>
     * There are only a few additional lifetime statistics for Left4Dead 2
     * which are not generated for Left4Dead, so this calls
     * <code>AbstractL4DStats#getLifetimeStats()</code> first and adds some
     * additional stats.
     *
     * @return The lifetime statistics of the player in Left4Dead 2
     */
    public Map<String, Object> getLifetimeStats() {
        if(!this.isPublic()) {
            return null;
        }

        if(this.lifetimeStats == null) {
            super.getLifetimeStats();
            Element lifetimeStatsElement = (Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("lifetime").item(0);
            this.lifetimeStats.put("avgAdrenalineShared", Float.parseFloat(lifetimeStatsElement.getElementsByTagName("adrenalineshared").item(0).getTextContent()));
            this.lifetimeStats.put("avgAdrenalineUsed", Float.parseFloat(lifetimeStatsElement.getElementsByTagName("adrenalineused").item(0).getTextContent()));
            this.lifetimeStats.put("avgDefibrillatorsUsed", Float.parseFloat(lifetimeStatsElement.getElementsByTagName("defibrillatorsused").item(0).getTextContent()));
        }

        return this.lifetimeStats;
    }

    /**
     * Returns a map of Scavenge statistics for this user like the number of
     * Scavenge rounds played
     * <p>
     * If the Scavenge statistics haven't been parsed already, parsing is done
     * now.
     *
     * @return The Scavenge statistics of the player
     */
    public Map<String, Object> getScavengeStats() {
        if(!this.isPublic()) {
            return null;
        }

        if(this.scavengeStats == null) {
            Element scavengeStatsElement = (Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("scavenge").item(0);
            this.scavengeStats = new HashMap<String, Object>();
            this.scavengeStats.put("avgCansPerRound", Float.parseFloat(scavengeStatsElement.getElementsByTagName("avgcansperround").item(0).getTextContent()));
            this.scavengeStats.put("perfectRounds", Integer.parseInt(scavengeStatsElement.getElementsByTagName("perfect16canrounds").item(0).getTextContent()));
            this.scavengeStats.put("roundsLost", Integer.parseInt(scavengeStatsElement.getElementsByTagName("roundslost").item(0).getTextContent()));
            this.scavengeStats.put("roundsPlayed", Integer.parseInt(scavengeStatsElement.getElementsByTagName("roundsplayed").item(0).getTextContent()));
            this.scavengeStats.put("roundsWon", Integer.parseInt(scavengeStatsElement.getElementsByTagName("roundswon").item(0).getTextContent()));
            this.scavengeStats.put("totalCans", Integer.parseInt(scavengeStatsElement.getElementsByTagName("totalcans").item(0).getTextContent()));

            HashMap<String, HashMap<String, Object>> mapsHash = new HashMap<String, HashMap<String, Object>>();
            NodeList mapNodes = scavengeStatsElement.getElementsByTagName("mapstats").item(0).getChildNodes();
            for(int i = 0; i < mapNodes.getLength(); i++) {
                Element mapData = (Element) mapNodes.item(i);
                String mapId = mapData.getElementsByTagName("name").item(0).getTextContent();
                HashMap<String, Object> mapHash = new HashMap<String, Object>();
                mapHash.put("avgRoundScore", Integer.parseInt(mapData.getElementsByTagName("avgscoreperround").item(0).getTextContent()));
                mapHash.put("highestGameScore", Integer.parseInt(mapData.getElementsByTagName("highgamescore").item(0).getTextContent()));
                mapHash.put("highestRoundScore", Integer.parseInt(mapData.getElementsByTagName("highroundscore").item(0).getTextContent()));
                mapHash.put("name", mapData.getElementsByTagName("fullname").item(0).getTextContent());
                mapHash.put("roundsPlayed", Integer.parseInt(mapData.getElementsByTagName("roundsplayed").item(0).getTextContent()));
                mapHash.put("roundsWon", Integer.parseInt(mapData.getElementsByTagName("roundswon").item(0).getTextContent()));
                mapsHash.put(mapId, mapHash);
            }
            this.scavengeStats.put("maps", mapsHash);
        }

        return this.scavengeStats;
    }

    /**
     * Returns a map of Survival statistics for this user like revived
     * teammates
     * <p>
     * If the Survival statistics haven't been parsed already, parsing is done
     * now.
     *
     * The XML layout for the Survival statistics for Left4Dead 2 differs a bit
     * from Left4Dead's Survival statistics. So we have to use a different way
     * of parsing for the maps and we use a different map class
     * (<code>L4D2Map</code>) which holds the additional information provided
     * in Left4Dead 2's statistics.
     *
     * @return The Survival statistics of the player
     */
    public Map<String, Object> getSurvivalStats()
            throws SteamCondenserException {
        if(!this.isPublic()) {
            return null;
        }

        if(this.survivalStats == null) {
            super.getSurvivalStats();
            Element survivalStatsElement = (Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("survival").item(0);
            HashMap<String, L4D2Map> mapsHash = new HashMap<String, L4D2Map>();
            NodeList mapNodes = survivalStatsElement.getElementsByTagName("maps").item(0).getChildNodes();
            for(int i = 0; i < mapNodes.getLength(); i++) {
                Element mapData = (Element) mapNodes.item(i);
                mapsHash.put(mapData.getNodeName(), new L4D2Map(mapData));
            }
            this.survivalStats.put("maps", mapsHash);
        }

        return this.survivalStats;
    }

    /**
     * Returns a map of <code>L4D2Weapon</code> for this user containing all
     * Left4Dead 2 weapons
     * <p>
     * If the weapons haven't been parsed already, parsing is done now.
     *
     * @return The weapon statistics for this player
     */
    public Map<String, GameWeapon> getWeaponStats() {
        if(!this.isPublic()) {
            return null;
        }

        if(this.weaponStats == null) {
            Element weaponStatsElement = (Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("weapons").item(0);
            this.weaponStats = new HashMap<String, GameWeapon>();
            NodeList weaponNodes = weaponStatsElement.getChildNodes();
            for(int i = 0; i < weaponNodes.getLength(); i++) {
                Element weaponData = (Element) weaponNodes.item(i);
                String weaponName = weaponData.getNodeName();
                GameWeapon weapon;
                if(!weaponName.equals("bilejars") && !weaponName.equals("molotov") && !weaponName.equals("pipes")) {
                    weapon = new L4D2Weapon(weaponData);
                }
                else {
                    weapon = new L4DExplosive(weaponData);
                }
                this.weaponStats.put(weaponName, weapon);
            }
        }

        return this.weaponStats;
    }
}
