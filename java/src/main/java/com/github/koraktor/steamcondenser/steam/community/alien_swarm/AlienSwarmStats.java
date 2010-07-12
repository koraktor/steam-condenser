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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.GameStats;

public class AlienSwarmStats extends GameStats {

    public static final String BASE_URL = "http://steamcommunity.com/public/images/gamestats/swarm/";

    private static final String[] WEAPONS = { "Autogun", "Cannon_Sentry", "Chainsaw",
        "Flamer", "Grenade_Launcher", "Hand_Grenades", "Hornet_Barrage",
        "Incendiary_Sentry", "Laser_Mines", "Marskman_Rifle", "Minigun",
        "Mining_Laser", "PDW", "Pistol", "Prototype_Rifle", "Rail_Rifle",
        "Rifle", "Rifle_Grenade", "Sentry_Gun", "Shotgun", "Tesla_Cannon",
        "Vindicator", "Vindicator_Grenade" };

    private Map<String, Object> favorites;

    private Map<String, Object> itemStats;

    private Map<String, Object> lifetimeStats;

    private Map<String, Object> missionStats;

    private Map<String, Object> weaponStats;

    public AlienSwarmStats(Object steamId) throws SteamCondenserException {
        super(steamId, "alienswarm");

        if(this.isPublic()) {
            Element lifetimeStats = (Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("lifetime").item(0);
            this.hoursPlayed = lifetimeStats.getElementsByTagName("timeplayed").item(0).getTextContent();

            this.lifetimeStats = new HashMap<String, Object>();
            this.lifetimeStats.put("accuracy", Float.valueOf(lifetimeStats.getElementsByTagName("accuracy").item(0).getTextContent()));
            this.lifetimeStats.put("aliensBurned", Integer.valueOf(lifetimeStats.getElementsByTagName("aliensburned").item(0).getTextContent()));
            this.lifetimeStats.put("aliensKilled", Integer.valueOf(lifetimeStats.getElementsByTagName("alienskilled").item(0).getTextContent()));
            this.lifetimeStats.put("campaigns", Integer.valueOf(lifetimeStats.getElementsByTagName("campaigns").item(0).getTextContent()));
            this.lifetimeStats.put("damageTaken", Integer.valueOf(lifetimeStats.getElementsByTagName("damagetaken").item(0).getTextContent()));
            this.lifetimeStats.put("experience", Integer.valueOf(lifetimeStats.getElementsByTagName("experience").item(0).getTextContent()));
            this.lifetimeStats.put("experienceRequired", Integer.valueOf(lifetimeStats.getElementsByTagName("xprequired").item(0).getTextContent()));
            this.lifetimeStats.put("fastHacks", Integer.valueOf(lifetimeStats.getElementsByTagName("fasthacks").item(0).getTextContent()));
            this.lifetimeStats.put("friendlyFire", Integer.valueOf(lifetimeStats.getElementsByTagName("friendlyfire").item(0).getTextContent()));
            this.lifetimeStats.put("gamesSuccessful", Integer.valueOf(lifetimeStats.getElementsByTagName("gamessuccess").item(0).getTextContent()));
            this.lifetimeStats.put("healing", Integer.valueOf(lifetimeStats.getElementsByTagName("healing").item(0).getTextContent()));
            this.lifetimeStats.put("killsPerHour", Float.valueOf(lifetimeStats.getElementsByTagName("killsperhour").item(0).getTextContent()));
            this.lifetimeStats.put("level", Integer.valueOf(lifetimeStats.getElementsByTagName("level").item(0).getTextContent()));
            this.lifetimeStats.put("promotion", Integer.valueOf(lifetimeStats.getElementsByTagName("promotion").item(0).getTextContent()));
            this.lifetimeStats.put("nextUnlock", lifetimeStats.getElementsByTagName("nextunlock").item(0).getTextContent());
            this.lifetimeStats.put("nextUnlockImg", BASE_URL + lifetimeStats.getElementsByTagName("nextunlockimg").item(0).getTextContent());
            this.lifetimeStats.put("shotsFired", Integer.valueOf(lifetimeStats.getElementsByTagName("shotsfired").item(0).getTextContent()));
            this.lifetimeStats.put("totalGames", Integer.valueOf(lifetimeStats.getElementsByTagName("totalgames").item(0).getTextContent()));

            if((Integer) this.lifetimeStats.get("promotion") > 0) {
                this.lifetimeStats.put("promotionImg", BASE_URL + lifetimeStats.getElementsByTagName("promotionpic").item(0).getTextContent());
            }

            this.lifetimeStats.put("games_successful_percentage", ((Integer) this.lifetimeStats.get("totalGames") > 0) ? (Integer) this.lifetimeStats.get("gamesSuccessful") / (Integer) this.lifetimeStats.get("totalGames") : 0);
        }
    }

    /**
     * Returns a Hash of favorites for this user like weapons and marine. If
     * the favorites haven"t been parsed already, parsing is done now.
     *
     * @return array
     */
    public Map<String, Object> getFavorites() {
        if(!this.isPublic()) {
            return null;
        }

        if(this.favorites == null) {
            Element favoritesData = (Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("favorites").item(0);

            this.favorites = new HashMap<String, Object>();
            this.favorites.put("class", favoritesData.getElementsByTagName("class").item(0).getTextContent());
            this.favorites.put("classImg", favoritesData.getElementsByTagName("classimg").item(0).getTextContent());
            this.favorites.put("classPercentage", Float.valueOf(favoritesData.getElementsByTagName("classpct").item(0).getTextContent()));
            this.favorites.put("difficulty", favoritesData.getElementsByTagName("difficulty").item(0).getTextContent());
            this.favorites.put("difficultyPercentage", Float.valueOf(favoritesData.getElementsByTagName("difficultypct").item(0).getTextContent()));
            this.favorites.put("extra", favoritesData.getElementsByTagName("extra").item(0).getTextContent());
            this.favorites.put("extraImg", favoritesData.getElementsByTagName("extraimg").item(0).getTextContent());
            this.favorites.put("extraPercentage", Float.valueOf(favoritesData.getElementsByTagName("extrapct").item(0).getTextContent()));
            this.favorites.put("marine", favoritesData.getElementsByTagName("marine").item(0).getTextContent());
            this.favorites.put("marineImg", favoritesData.getElementsByTagName("marineimg").item(0).getTextContent());
            this.favorites.put("marinePercentage", Float.valueOf(favoritesData.getElementsByTagName("marinepct").item(0).getTextContent()));
            this.favorites.put("mission", favoritesData.getElementsByTagName("mission").item(0).getTextContent());
            this.favorites.put("missionImg", favoritesData.getElementsByTagName("missionimg").item(0).getTextContent());
            this.favorites.put("missionPercentage", Float.valueOf(favoritesData.getElementsByTagName("missionpct").item(0).getTextContent()));
            this.favorites.put("primaryWeapon", favoritesData.getElementsByTagName("primary").item(0).getTextContent());
            this.favorites.put("primaryWeaponImg", favoritesData.getElementsByTagName("primaryimg").item(0).getTextContent());
            this.favorites.put("primaryWeaponPercentage", Float.valueOf(favoritesData.getElementsByTagName("primarypct").item(0).getTextContent()));
            this.favorites.put("secondaryWeapon", favoritesData.getElementsByTagName("secondary").item(0).getTextContent());
            this.favorites.put("secondaryWeaponImg", favoritesData.getElementsByTagName("secondaryimg").item(0).getTextContent());
            this.favorites.put("secondaryWeapon_Percentage", Float.valueOf(favoritesData.getElementsByTagName("secondarypct").item(0).getTextContent()));
        }

        return this.favorites;
    }

    /**
     * Returns an array of item stats for this user like ammo deployed and
     * medkits used. If the items haven"t been parsed already, parsing is done
     * now.
     *
     * @return array
     */
    public Map<String, Object> getItemStats() {
        if(!this.isPublic()) {
            return null;
        }

        if(this.itemStats == null) {
            Element itemStatsData = (Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("weapons").item(0);

            this.itemStats = new HashMap<String, Object>();
            this.itemStats.put("ammoDeployed", Integer.valueOf(itemStatsData.getElementsByTagName("ammo_deployed").item(0).getTextContent()));
            this.itemStats.put("sentrygunsDeployed", Integer.valueOf(itemStatsData.getElementsByTagName("sentryguns_deployed").item(0).getTextContent()));
            this.itemStats.put("sentryFlamersDeployed", Integer.valueOf(itemStatsData.getElementsByTagName("sentry_flamers_deployed").item(0).getTextContent()));
            this.itemStats.put("sentryFreezeDeployed", Integer.valueOf(itemStatsData.getElementsByTagName("sentry_freeze_deployed").item(0).getTextContent()));
            this.itemStats.put("sentryCannonDeployed", Integer.valueOf(itemStatsData.getElementsByTagName("sentry_cannon_deployed").item(0).getTextContent()));
            this.itemStats.put("medkitsUsed", Integer.valueOf(itemStatsData.getElementsByTagName("medkits_used").item(0).getTextContent()));
            this.itemStats.put("flaresUsed", Integer.valueOf(itemStatsData.getElementsByTagName("flares_used").item(0).getTextContent()));
            this.itemStats.put("adrenalineUsed", Integer.valueOf(itemStatsData.getElementsByTagName("adrenaline_used").item(0).getTextContent()));
            this.itemStats.put("teslaTrapsDeployed", Integer.valueOf(itemStatsData.getElementsByTagName("tesla_traps_deployed").item(0).getTextContent()));
            this.itemStats.put("freezeGrenadesThrown", Integer.valueOf(itemStatsData.getElementsByTagName("freeze_grenades_thrown").item(0).getTextContent()));
            this.itemStats.put("electricArmorUsed", Integer.valueOf(itemStatsData.getElementsByTagName("electric_armor_used").item(0).getTextContent()));
            this.itemStats.put("healgunHeals", Integer.valueOf(itemStatsData.getElementsByTagName("healgun_heals").item(0).getTextContent()));
            this.itemStats.put("healgunHealsSelf", Integer.valueOf(itemStatsData.getElementsByTagName("healgun_heals_self").item(0).getTextContent()));
            this.itemStats.put("healbeaconHeals", Integer.valueOf(itemStatsData.getElementsByTagName("healbeacon_heals").item(0).getTextContent()));
            this.itemStats.put("healbeaconHealsSelf", Integer.valueOf(itemStatsData.getElementsByTagName("healbeacon_heals_self").item(0).getTextContent()));
            this.itemStats.put("damageAmpsUsed", Integer.valueOf(itemStatsData.getElementsByTagName("damage_amps_used").item(0).getTextContent()));
            this.itemStats.put("healbeaconsDeployed", Integer.valueOf(itemStatsData.getElementsByTagName("healbeacons_deployed").item(0).getTextContent()));
            this.itemStats.put("healbeaconHealsPct", Float.valueOf(itemStatsData.getElementsByTagName("healbeacon_heals_pct").item(0).getTextContent()));
            this.itemStats.put("healgunHealsPct", Float.valueOf(itemStatsData.getElementsByTagName("healgun_heals_pct").item(0).getTextContent()));
            this.itemStats.put("healbeaconHealsPctSelf", Float.valueOf(itemStatsData.getElementsByTagName("healbeacon_heals_pct_self").item(0).getTextContent()));
            this.itemStats.put("healgunHealsPctSelf", Float.valueOf(itemStatsData.getElementsByTagName("healgun_heals_pct_self").item(0).getTextContent()));
        }

        return this.itemStats;
    }

    /**
     * Returns an array of AlienSwarmMission for this user containing all Alien
     * Swarm missions. If the missions haven"t been parsed already, parsing is
     * done now.
     *
     * @return array
     */
    public Map<String, Object> getMissionStats() {
        if(!this.isPublic()) {
            return null;
        }

        if(this.missionStats == null) {
            this.missionStats = new HashMap<String, Object>();
            Element statsElement = (Element) this.xmlData.getElementsByTagName("stats").item(0);
            NodeList missionNodes = statsElement.getElementsByTagName("missions").item(0).getChildNodes();
            for(int i = 0; i < missionNodes.getLength(); i++) {
                Node missionNode = missionNodes.item(i);
                if(missionNode.getNodeType() == Node.TEXT_NODE) {
                    continue;
                }
                this.missionStats.put(missionNode.getNodeName(), new AlienSwarmMission((Element) missionNode));
            }
        }

        return this.missionStats;
    }

    /**
     * Returns an array of AlienSwarmWeapon for this user containing all Alien
     * Swarm weapons. If the weapons haven"t been parsed already, parsing is
     * done now.
     */
    public Map<String, Object> getWeaponStats() {
        if(!this.isPublic()) {
            return null;
        }

        if(this.weaponStats == null) {
            this.weaponStats = new HashMap<String, Object>();
            for(String weaponNode : WEAPONS) {
                Element weaponData = (Element) ((Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("weapons").item(0)).getElementsByTagName(weaponNode).item(0);
                AlienSwarmWeapon weapon = new AlienSwarmWeapon(weaponData);
                this.weaponStats.put(weapon.getName(), weapon);
            }
        }

        return this.weaponStats;
    }

}
