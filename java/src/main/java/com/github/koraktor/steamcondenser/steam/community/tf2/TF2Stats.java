/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.tf2;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.GameStats;

/**
 * The TF2Stats class represents the game statistics for a single user in Team
 * Fortress 2
 *
 * @author Sebastian Staudt
 */
public class TF2Stats extends GameStats {

	private int accumulatedPoints;
	private ArrayList<TF2Class> classStats;

	/**
	 * Creates a new object holding Team Fortress 2 statistics for the given
	 * user
	 * @param steamId
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public TF2Stats(Object steamId)
			throws SteamCondenserException {
		super(steamId, "tf2");

		if(this.isPublic()) {
			this.accumulatedPoints = Integer.parseInt(((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("accumulatedPoints").item(0).getTextContent());
		}
	}

	public int getAccumulatedPoints() {
		return this.accumulatedPoints;
	}

	/**
	 * Returns the statistics for all Team Fortress 2 classes for this user
	 * @return An array storing individual TF2Class objects for each Team
	 *         Fortress 2 class
	 */
	public ArrayList<TF2Class> getClassStats() {
		if(!this.isPublic()) {
			return null;
		}

		if(this.classStats == null) {
			this.classStats = new ArrayList<TF2Class>();
			NodeList classes = ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("classData");
			for(int i = 0; i < classes.getLength(); i++) {
				this.classStats.add(TF2ClassFactory.getTF2Class((Element) classes.item(i)));
			}
		}

		return this.classStats;
	}
}
