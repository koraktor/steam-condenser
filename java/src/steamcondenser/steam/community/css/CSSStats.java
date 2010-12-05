/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010, Sebastian Staudt
 */

package steamcondenser.steam.community.css;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import steamcondenser.SteamCondenserException;
import steamcondenser.steam.community.GameStats;

/**
 * The CSSStats class represents the game statistics for a single user in
 * Counter-Strike: Source
 */
public class CSSStats extends GameStats {

    private static final String[] MAPS = { "cs_assault", "cs_compound",
           "cs_havana", "cs_italy", "cs_militia", "cs_office", "de_aztec",
           "de_cbble", "de_chateau", "de_dust", "de_dust2", "de_inferno",
           "de_nuke", "de_piranesi", "de_port", "de_prodigy", "de_tides",
           "de_train" } ;

    private static final String[] WEAPONS = { "deagle", "usp", "glock", "p228",
            "elite", "fiveseven", "awp", "ak47", "m4a1", "aug", "sg552",
            "sg550", "galil", "famas", "scout", "g3sg1", "p90", "mp5navy",
            "tmp", "mac10", "ump45", "m3", "xm1014", "m249", "knife",
            "grenade" };

    private Map<String, Object> lastMatchStats;

    private Map<String, CSSMap> mapStats;

    private Map<String, Object> totalStats;

    private Map<String, CSSWeapon> weaponStats;

    /**
     * Creates a CSSStats object by calling the super constructor with the game
     * name "cs:s"
     * @param steamId The custom URL or the 64bit Steam ID of the user
     * @throws SteamCondenserException If an error occurs
     */
    public CSSStats(Object steamId) throws SteamCondenserException {
        super(steamId, "cs:s");

        if(this.isPublic()); {
            Element lastMatchStats = (Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("lastmatch").item(0);
            Element lifetimeStats = (Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("lifetime").item(0);
            Element summaryStats = (Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("summary").item(0);

            this.lastMatchStats = new HashMap<String, Object>();
            this.totalStats     = new HashMap<String, Object>();

            this.lastMatchStats.put("costPerKill", Float.parseFloat(lastMatchStats.getElementsByTagName("costkill").item(0).getTextContent()));
            this.lastMatchStats.put("ctWins", Integer.parseInt(lastMatchStats.getElementsByTagName("ct_wins").item(0).getTextContent()));
            this.lastMatchStats.put("damage", Integer.parseInt(lastMatchStats.getElementsByTagName("dmg").item(0).getTextContent()));
            this.lastMatchStats.put("deaths", Integer.parseInt(lastMatchStats.getElementsByTagName("deaths").item(0).getTextContent()));
            this.lastMatchStats.put("dominations", Integer.parseInt(lastMatchStats.getElementsByTagName("dominations").item(0).getTextContent()));
            this.lastMatchStats.put("favoriteWeaponId", Integer.parseInt(lastMatchStats.getElementsByTagName("favwpnid").item(0).getTextContent()));
            this.lastMatchStats.put("kills", Integer.parseInt(lastMatchStats.getElementsByTagName("kills").item(0).getTextContent()));
            this.lastMatchStats.put("maxPlayers", Integer.parseInt(lastMatchStats.getElementsByTagName("max_players").item(0).getTextContent()));
            this.lastMatchStats.put("money", Integer.parseInt(lastMatchStats.getElementsByTagName("money").item(0).getTextContent()));
            this.lastMatchStats.put("revenges", Integer.parseInt(lastMatchStats.getElementsByTagName("revenges").item(0).getTextContent()));
            this.lastMatchStats.put("stars", Integer.parseInt(lastMatchStats.getElementsByTagName("stars").item(0).getTextContent()));
            this.lastMatchStats.put("tWins", Integer.parseInt(lastMatchStats.getElementsByTagName("t_wins").item(0).getTextContent()));
            this.lastMatchStats.put("wins", Integer.parseInt(lastMatchStats.getElementsByTagName("wins").item(0).getTextContent()));
            this.totalStats.put("blindKills", Integer.parseInt(lifetimeStats.getElementsByTagName("blindkills").item(0).getTextContent()));
            this.totalStats.put("bombsDefused", Integer.parseInt(lifetimeStats.getElementsByTagName("bombsdefused").item(0).getTextContent()));
            this.totalStats.put("bombsPlanted", Integer.parseInt(lifetimeStats.getElementsByTagName("bombsplanted").item(0).getTextContent()));
            this.totalStats.put("damage", Integer.parseInt(lifetimeStats.getElementsByTagName("dmg").item(0).getTextContent()));
            this.totalStats.put("deaths", Integer.parseInt(summaryStats.getElementsByTagName("deaths").item(0).getTextContent()));
            this.totalStats.put("dominationOverkills", Integer.parseInt(lifetimeStats.getElementsByTagName("dominationoverkills").item(0).getTextContent()));
            this.totalStats.put("dominations", Integer.parseInt(lifetimeStats.getElementsByTagName("dominations").item(0).getTextContent()));
            this.totalStats.put("earnedMoney", Integer.parseInt(lifetimeStats.getElementsByTagName("money").item(0).getTextContent()));
            this.totalStats.put("enemyWeaponKills", Integer.parseInt(lifetimeStats.getElementsByTagName("enemywpnkills").item(0).getTextContent()));
            this.totalStats.put("headshots", Integer.parseInt(lifetimeStats.getElementsByTagName("headshots").item(0).getTextContent()));
            this.totalStats.put("hits", Integer.parseInt(summaryStats.getElementsByTagName("shotshit").item(0).getTextContent()));
            this.totalStats.put("hostagesRescued", Integer.parseInt(lifetimeStats.getElementsByTagName("hostagesrescued").item(0).getTextContent()));
            this.totalStats.put("kills", Integer.parseInt(summaryStats.getElementsByTagName("kills").item(0).getTextContent()));
            this.totalStats.put("knifeKills", Integer.parseInt(lifetimeStats.getElementsByTagName("knifekills").item(0).getTextContent()));
            this.totalStats.put("logosSprayed", Integer.parseInt(lifetimeStats.getElementsByTagName("decals").item(0).getTextContent()));
            this.totalStats.put("nightvisionDamage", Integer.parseInt(lifetimeStats.getElementsByTagName("nvgdmg").item(0).getTextContent()));
            this.totalStats.put("pistolRoundsWon", Integer.parseInt(lifetimeStats.getElementsByTagName("pistolrounds").item(0).getTextContent()));
            this.totalStats.put("revenges", Integer.parseInt(lifetimeStats.getElementsByTagName("revenges").item(0).getTextContent()));
            this.totalStats.put("roundsPlayed", Integer.parseInt(summaryStats.getElementsByTagName("rounds").item(0).getTextContent()));
            this.totalStats.put("roundsWon", Integer.parseInt(summaryStats.getElementsByTagName("wins").item(0).getTextContent()));
            this.totalStats.put("secondsPlayed", Integer.parseInt(summaryStats.getElementsByTagName("timeplayed").item(0).getTextContent()));
            this.totalStats.put("shots", Integer.parseInt(summaryStats.getElementsByTagName("shots").item(0).getTextContent()));
            this.totalStats.put("stars", Integer.parseInt(summaryStats.getElementsByTagName("stars").item(0).getTextContent()));
            this.totalStats.put("weaponsDonated", Integer.parseInt(lifetimeStats.getElementsByTagName("wpndonated").item(0).getTextContent()));
            this.totalStats.put("windowsBroken", Integer.parseInt(lifetimeStats.getElementsByTagName("winbroken").item(0).getTextContent()));
            this.totalStats.put("zoomedSniperKills", Integer.parseInt(lifetimeStats.getElementsByTagName("zsniperkills").item(0).getTextContent()));

            if((Integer) this.lastMatchStats.get("deaths") > 0) {
                this.lastMatchStats.put("kdratio", (Integer) this.lastMatchStats.get("kills") / (Integer) this.lastMatchStats.get("deaths"));
            } else {
                this.lastMatchStats.put("kdratio", 0);
            }
            if((Integer) this.totalStats.get("shots") > 0) {
                this.totalStats.put("accuracy", (Integer) this.totalStats.get("hits") / (Integer) this.totalStats.get("shots"));
            } else {
                this.totalStats.put("accuracy", 0);
            }
            if((Integer) this.totalStats.get("deaths") > 0) {
                this.totalStats.put("kdratio", (Integer) this.totalStats.get("kills") / (Integer) this.totalStats.get("deaths"));
            } else {
                this.totalStats.put("kdratio", 0);
            }
            this.totalStats.put("roundsLost", (Integer) this.totalStats.get("roundsPlayed") - (Integer) this.totalStats.get("roundsWon"));
        }
    }

    public Map<String, Object> getLastMatchStats() {
        return this.lastMatchStats;
    }

    public Map<String, CSSMap> getMapStats() {
        if(!this.isPublic()) {
            return null;
        }

        if(this.mapStats == null) {
            this.mapStats = new HashMap<String, CSSMap>();
            Element mapsData = (Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("maps").item(0);

            for(String mapName : MAPS) {
                this.mapStats.put(mapName, new CSSMap(mapName, mapsData));
            }
        }

        return this.mapStats;
    }

    public Map<String, Object> getTotalStats() {
        return this.totalStats;
    }

    public Map<String, CSSWeapon> getWeaponStats() {
        if(!this.isPublic()) {
            return null;
        }

        if(this.weaponStats == null) {
            this.weaponStats = new HashMap<String, CSSWeapon>();
            Element weaponData = (Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("weapons").item(0);

            for(String weaponName : WEAPONS) {
                this.weaponStats.put(weaponName, new CSSWeapon(weaponName, weaponData));
            }
        }

        return this.weaponStats;
    }

}
