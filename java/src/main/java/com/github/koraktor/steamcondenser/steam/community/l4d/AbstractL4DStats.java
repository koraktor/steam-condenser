/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.l4d;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.GameStats;
import com.github.koraktor.steamcondenser.steam.community.GameWeapon;

/**
 * AbstractL4DStats is an abstract base class for statistics for Left4Dead and
 * Left4Dead 2. As both games have more or less the same statistics available
 * in the Steam Community the code for both is pretty much the same.
 */
public abstract class AbstractL4DStats extends GameStats {

	protected HashMap<String, Object> favorites;

	protected HashMap<String, Object> lifetimeStats;

	protected HashMap<String, Object> mostRecentGame;

	protected HashMap<String, Object> survivalStats;

	protected HashMap<String, Object> teamplayStats;

	protected HashMap<String, Object> versusStats;

	protected HashMap<String, GameWeapon> weaponStats;

	/**
	 * AbstractL4DStats is an abstract base class for statistics for Left4Dead
     * and Left4Dead 2. As both games have more or less the same statistics
     * available in the Steam Community the code for both is pretty much the
     * same.
     *
	 * @param steamId The custom URL or the 64bit Steam ID of the user
	 * @throws SteamCondenserException
	 */
	public AbstractL4DStats(Object steamId, String gameName)
			throws SteamCondenserException {
		super(steamId, gameName);

		if(this.isPublic()) {
			Element mostRecentGameNode = (Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("mostRecentGame").item(0);
			this.mostRecentGame = new HashMap<String, Object>();
            if(mostRecentGameNode != null) {
                this.mostRecentGame.put("difficulty", mostRecentGameNode.getElementsByTagName("difficulty").item(0).getTextContent());
                this.mostRecentGame.put("escaped", mostRecentGameNode.getElementsByTagName("bEscaped").item(0).getTextContent().equals("1"));
                this.mostRecentGame.put("movie", mostRecentGameNode.getElementsByTagName("movie").item(0).getTextContent());
                this.mostRecentGame.put("timePlayed", mostRecentGameNode.getElementsByTagName("time").item(0).getTextContent());
            }
		}
	}

	/**
	 * @return A HashMap of favorites for this user like weapons and character.
	 * If the favorites haven't been parsed already, parsing is done now.
	 */
	public HashMap<String, Object> getFavorites() {
		if(!this.isPublic()) {
			return null;
		}

		if(this.favorites == null) {
			Element favoritesNode = (Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("favorites").item(0);
			this.favorites = new HashMap<String, Object>();
			this.favorites.put("campaign", favoritesNode.getElementsByTagName("campaign").item(0).getTextContent());
			this.favorites.put("campaignPercentage", Integer.parseInt(favoritesNode.getElementsByTagName("campaignpct").item(0).getTextContent()));
			this.favorites.put("character", favoritesNode.getElementsByTagName("character").item(0).getTextContent());
			this.favorites.put("characterPercentage", Integer.parseInt(favoritesNode.getElementsByTagName("characterpct").item(0).getTextContent()));
			this.favorites.put("level1Weapon", favoritesNode.getElementsByTagName("weapon1").item(0).getTextContent());
			this.favorites.put("level1WeaponPercentage", Integer.parseInt(favoritesNode.getElementsByTagName("weapon1pct").item(0).getTextContent()));
			this.favorites.put("level2Weapon", favoritesNode.getElementsByTagName("weapon2").item(0).getTextContent());
			this.favorites.put("level2WeaponPercentage", Integer.parseInt(favoritesNode.getElementsByTagName("weapon2pct").item(0).getTextContent()));
		}

		return this.favorites;
	}

	/**
	 * @return A HashMap of lifetime statistics for this user like the time
	 * played.
	 * If the lifetime statistics haven't been parsed already, parsing is done
	 * now.
	 */
	public HashMap<String, Object> getLifetimeStats() {
		if(!this.isPublic()) {
			return null;
		}

		if(this.lifetimeStats == null) {
			Element lifetimeStatsElement = (Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("lifetime").item(0);
			this.lifetimeStats = new HashMap<String, Object>();
			this.lifetimeStats.put("finalesSurvived", Integer.parseInt(lifetimeStatsElement.getElementsByTagName("finales").item(0).getTextContent()));
			this.lifetimeStats.put("gamesPlayed", Integer.parseInt(lifetimeStatsElement.getElementsByTagName("gamesplayed").item(0).getTextContent()));
			this.lifetimeStats.put("finalesSurvivedPercentage", (Float) this.lifetimeStats.get("finalesSurvived") / (Integer) this.lifetimeStats.get("gamesPlayed"));
			this.lifetimeStats.put("infectedKilled", Integer.parseInt(lifetimeStatsElement.getElementsByTagName("infectedkilled").item(0).getTextContent()));
			this.lifetimeStats.put("killsPerHour", Float.parseFloat(lifetimeStatsElement.getElementsByTagName("killsperhour").item(0).getTextContent()));
			this.lifetimeStats.put("avgKitsShared", Float.parseFloat(lifetimeStatsElement.getElementsByTagName("kitsshared").item(0).getTextContent()));
			this.lifetimeStats.put("avgKitsUsed", Float.parseFloat(lifetimeStatsElement.getElementsByTagName("kitsused").item(0).getTextContent()));
			this.lifetimeStats.put("avgPillsShared", Float.parseFloat(lifetimeStatsElement.getElementsByTagName("pillsshared").item(0).getTextContent()));
			this.lifetimeStats.put("avgPillsUsed", Float.parseFloat(lifetimeStatsElement.getElementsByTagName("pillsused").item(0).getTextContent()));
			this.lifetimeStats.put("timePlayed", lifetimeStatsElement.getElementsByTagName("timeplayed").item(0).getTextContent());
		}

		return this.lifetimeStats;
	}

	/**
	 * @return A HashMap of Survival statistics for this user like revived
	 * teammates.
	 * If the Survival statistics haven't been parsed already, parsing is done
	 * now.
	 */
	public HashMap<String, Object> getSurvivalStats()
            throws SteamCondenserException {
		if(!this.isPublic()) {
			return null;
		}

		if(this.survivalStats == null) {
			Element survivalStatsElement = (Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("survival").item(0);
			this.survivalStats = new HashMap<String, Object>();
			this.survivalStats.put("goldMedals", Integer.parseInt(survivalStatsElement.getElementsByTagName("goldmedals").item(0).getTextContent()));
			this.survivalStats.put("silverMedals", Integer.parseInt(survivalStatsElement.getElementsByTagName("silvermedals").item(0).getTextContent()));
			this.survivalStats.put("bronzeMedals", Integer.parseInt(survivalStatsElement.getElementsByTagName("bronzemedals").item(0).getTextContent()));
			this.survivalStats.put("roundsPlayed", Integer.parseInt(survivalStatsElement.getElementsByTagName("roundsplayed").item(0).getTextContent()));
			this.survivalStats.put("bestTime", Float.parseFloat(survivalStatsElement.getElementsByTagName("besttime").item(0).getTextContent()));

			HashMap<String, L4DMap> mapsHash = new HashMap<String, L4DMap>();
			NodeList mapNodes = survivalStatsElement.getElementsByTagName("maps").item(0).getChildNodes();
			for(int i = 0; i < mapNodes.getLength(); i++) {
				Element mapData = (Element) mapNodes.item(i);
				mapsHash.put(mapData.getNodeName(), new L4DMap(mapData));
			}
			this.survivalStats.put("maps", mapsHash);
		}

		return this.survivalStats;
	}

	/**
	 * @return A HashMap of teamplay statistics for this user like revived
	 * teammates.
	 * If the teamplay statistics haven't been parsed already, parsing is done
	 * now.
	 */
	public HashMap<String, Object> getTeamplayStats() {
		if(!this.isPublic()) {
			return null;
		}

		if(this.teamplayStats == null) {
			Element teamplayStatsElement = (Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("teamplay").item(0);
			this.teamplayStats = new HashMap<String, Object>();
			this.teamplayStats.put("revived", Integer.parseInt(teamplayStatsElement.getElementsByTagName("revived").item(0).getTextContent()));
			this.teamplayStats.put("mostRevivedDifficulty", teamplayStatsElement.getElementsByTagName("reviveddiff").item(0).getTextContent());
			this.teamplayStats.put("avgRevived", Float.parseFloat(teamplayStatsElement.getElementsByTagName("revivedavg").item(0).getTextContent()));
			this.teamplayStats.put("avgWasRevived", Float.parseFloat(teamplayStatsElement.getElementsByTagName("wasrevivedavg").item(0).getTextContent()));
			this.teamplayStats.put("protected", Integer.parseInt(teamplayStatsElement.getElementsByTagName("protected").item(0).getTextContent()));
			this.teamplayStats.put("mostProtectedDifficulty", teamplayStatsElement.getElementsByTagName("protecteddiff").item(0).getTextContent());
			this.teamplayStats.put("avgProtected", Float.parseFloat(teamplayStatsElement.getElementsByTagName("avgProtected").item(0).getTextContent()));
			this.teamplayStats.put("avgWasProtected", Float.parseFloat(teamplayStatsElement.getElementsByTagName("avgWasProtected").item(0).getTextContent()));
			this.teamplayStats.put("friendlyFireDamage", Integer.parseInt(teamplayStatsElement.getElementsByTagName("ffdamage").item(0).getTextContent()));
			this.teamplayStats.put("mostFriendlyFireDamageDifficulty", teamplayStatsElement.getElementsByTagName("ffdamagediff").item(0).getTextContent());
			this.teamplayStats.put("avgFriendlyFireDamage", Float.parseFloat(teamplayStatsElement.getElementsByTagName("ffdamageavg").item(0).getTextContent()));
		}

		return this.teamplayStats;
	}

	/**
	 * @return A HashMap of Versus statistics for this user like percentage of
	 * rounds won.
	 * If the Versus statistics haven't been parsed already, parsing is done
     * now.
	 */
	public HashMap<String, Object> getVersusStats() {
		if(!this.isPublic()) {
			return null;
		}

		if(this.versusStats == null) {
			Element versusStatsElement = (Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("versus").item(0);
			this.versusStats = new HashMap<String, Object>();
			this.versusStats.put("gamesPlayed", Integer.parseInt(versusStatsElement.getElementsByTagName("gamesplayed").item(0).getTextContent()));
			this.versusStats.put("gamesCompleted", Integer.parseInt(versusStatsElement.getElementsByTagName("gamescompleted").item(0).getTextContent()));
			this.versusStats.put("finalesSurvived", Integer.parseInt(versusStatsElement.getElementsByTagName("finales").item(0).getTextContent()));
			this.versusStats.put("finalesSurvivedPercentage", (Integer) this.versusStats.get("finalesSurvived") / (Integer) this.versusStats.get("gamesPlayed"));
			this.versusStats.put("points", Integer.parseInt(versusStatsElement.getElementsByTagName("points").item(0).getTextContent()));
			this.versusStats.put("mostPointsInfected", versusStatsElement.getElementsByTagName("pointsas").item(0).getTextContent());
			this.versusStats.put("gamesWon", Integer.parseInt(versusStatsElement.getElementsByTagName("gameswon").item(0).getTextContent()));
			this.versusStats.put("gamesLost", Integer.parseInt(versusStatsElement.getElementsByTagName("gameslost").item(0).getTextContent()));
			this.versusStats.put("highestSurvivorScore", Integer.parseInt(versusStatsElement.getElementsByTagName("survivorscore").item(0).getTextContent()));

			ArrayList<String> infectedArray = new ArrayList<String>();
			infectedArray.add("boomer");
			infectedArray.add("hunter");
			infectedArray.add("smoker");
			infectedArray.add("tank");
			for(String infected : infectedArray) {
				HashMap<String, Number> infectedStats = new HashMap<String, Number>();
				infectedStats.put("special", Integer.parseInt(versusStatsElement.getElementsByTagName(infected + "special").item(0).getTextContent()));
				infectedStats.put("mostDamage", Integer.parseInt(versusStatsElement.getElementsByTagName(infected + "dmg").item(0).getTextContent()));
				infectedStats.put("avgLifespan", Integer.parseInt(versusStatsElement.getElementsByTagName(infected + "lifespan").item(0).getTextContent()));
				this.versusStats.put(infected, infectedStats);
			}
		}

		return this.versusStats;
	}
}
