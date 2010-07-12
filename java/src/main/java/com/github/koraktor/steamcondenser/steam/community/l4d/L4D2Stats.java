/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.l4d;

import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.GameWeapon;

public class L4D2Stats extends AbstractL4DStats {

    private HashMap<String, Object> scavengeStats;

    /**
     * Creates a L4D2Stats object by calling the super constructor with the game
     * name "l4d2"
     * @param steamId The custom URL or the 64bit Steam ID of the user
     * @throws SteamCondenserException
     */
    public L4D2Stats(Object steamId)
            throws SteamCondenserException {
        super(steamId, "l4d2");
    }

    /**
     * @return A HashMap of lifetime statistics for this user like the time
     * played.
     * If the lifetime statistics haven't been parsed already, parsing is done
     * now.
     *
     * There are only a few additional lifetime statistics for Left4Dead 2
     * which are not generated for Left4Dead, so this calls
     * AbstractL4DStats#getLifetimeStats() first and adds some additional stats.
     */
    public HashMap<String, Object> getLifetimeStats() {
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
     * @return A HashMap of Scavenge statistics for this user like the number of
     * Scavenge rounds played.
     * If the Scavenge statistics haven't been parsed already, parsing is done
     * now.
     */
    public HashMap<String, Object> getScavengeStats() {
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
     * @return A HashMap of Survival statistics for this user like revived
     * teammates.
     * If the Survival statistics haven't been parsed already, parsing is done
     * now.
     *
     * The XML layout for the Survival statistics for Left4Dead 2 differs a bit
     * from Left4Dead's Survival statistics. So we have to use a different way
     * of parsing for the maps and we use a different map class (L4D2Map) which
     * holds the additional information provided in Left4Dead 2's statistics.
     */
    public HashMap<String, Object> getSurvivalStats()
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
     * @return A HashMap of L4D2Weapon for this user containing all Left4Dead 2
     * weapons.
     * If the weapons haven't been parsed already, parsing is done now.
     */
    public HashMap<String, GameWeapon> getWeaponStats() {
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
