/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009-2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.l4d;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.GameWeapon;

public class L4DStats extends AbstractL4DStats {

	/**
	 * Creates a L4DStats object by calling the super constructor with the game
	 * name "l4d"
	 * @param steamId The custom URL or the 64bit Steam ID of the user
     * @throws SteamCondenserException If an error occurs
	 */
	public L4DStats(Object steamId)
			throws SteamCondenserException {
		super(steamId, "l4d");
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
            super.getSurvivalStats();
            Element survivalStatsElement = (Element) ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("survival").item(0);
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
	 * @return A HashMap of L4DWeapon for this user containing all Left4Dead
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
				if(!weaponName.equals("molotov") && !weaponName.equals("pipes")) {
					weapon = new L4DWeapon(weaponData);
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
