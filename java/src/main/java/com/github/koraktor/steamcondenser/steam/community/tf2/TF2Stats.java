/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.tf2;

import java.util.ArrayList;

import org.json.JSONException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.exceptions.WebApiException;
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

    private TF2Inventory inventory;

	/**
	 * Creates a new object holding Team Fortress 2 statistics for the given
	 * user

     * @param steamId The custom URL or 64bit Steam ID of the user
     * @throws SteamCondenserException If an error occurs while fetching the
     *                                 stats data
	 */
	public TF2Stats(Object steamId)
			throws SteamCondenserException {
		super(steamId, "tf2");

		if(this.isPublic()) {
			this.accumulatedPoints = Integer.parseInt(((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("accumulatedPoints").item(0).getTextContent());
		}
	}
    /**
     * Returns the total points this player has achieved in his career
     *
     * @return This player's accumulated points
     */
	public int getAccumulatedPoints() {
		return this.accumulatedPoints;
	}

	/**
	 * Returns the statistics for all Team Fortress 2 classes for this user
     *
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

    /**
     * Returns the current Team Fortress 2 inventory (a.k.a. backpack) of this
     * player
     *
     * @return This player's TF2 backpack
     * @throws JSONException If the received data could not be parsed
     * @throws WebApiException If an error occured while querying Steam's Web
     *                         API
     */
    public TF2Inventory getInventory()
            throws JSONException, WebApiException {
        if(!this.isPublic()) {
            return null;
        }

        if(this.inventory == null) {
            this.inventory = TF2Inventory.create(this.steamId64);
        }

        return this.inventory;
    }
}
