/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.dods;

import java.util.HashMap;


import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.GameStats;

/**
 * The DoDSStats class represents the game statistics for a single user in Day
 * of Defeat: Source
 *
 * @author Sebastian Staudt
 */
public class DoDSStats extends GameStats {

	private HashMap<String, DoDSClass> classStats;
	private HashMap<String, DoDSWeapon> weaponStats;

	/**
	 * Creates a DoDSStats object by calling the super constructor with the game
	 * name "DoD:S"
	 *
	 * @param steamId
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public DoDSStats(Object steamId)
			throws SteamCondenserException {
		super(steamId, "dod:s");
	}

	/**
	 * Returns an associative array of DoDSClass for this user containing all
	 * Day of Defeat: Source classes. If the classes haven't been parsed
	 * already, parsing is done now.
	 *
	 * @return DoDSClass[]
	 */
	public HashMap<String, DoDSClass> getClassStats() {
		if(!this.isPublic()) {
			return null;
		}

		if(this.classStats == null) {
			this.classStats = new HashMap<String, DoDSClass>();
			NodeList classNodes = ((Element) this.xmlData.getElementsByTagName("classes").item(0)).getElementsByTagName("class");
			for(int i = 0; i < classNodes.getLength(); i++) {
				Element classData = (Element) classNodes.item(i);
				this.classStats.put(classData.getAttribute("key"),
						new DoDSClass(classData));
			}
		}

		return this.classStats;
	}

	/**
	 * Returns an associative array of DoDSWeapon for this user containing all
	 * DoD:S weapons. If the weapons haven't been parsed already, parsing is
	 * done now.
	 *
	 * @return DoDSWeapon[]
	 */
	public HashMap<String, DoDSWeapon> getWeaponStats() {
		if(!this.isPublic()) {
			return null;
		}

		if(this.weaponStats == null) {
			this.weaponStats = new HashMap<String, DoDSWeapon>();
			NodeList weaponNodes = ((Element) this.xmlData.getElementsByTagName("weapons").item(0)).getChildNodes();
			for(int i = 0; i < weaponNodes.getLength(); i++) {
				Element weaponData = (Element) weaponNodes.item(i);
				this.weaponStats.put(weaponData.getAttribute("key"),
						new DoDSWeapon(weaponData));
			}
		}

		return this.weaponStats;
	}
}
