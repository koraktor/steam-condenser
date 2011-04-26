/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.portal2;

import org.json.JSONException;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.exceptions.WebApiException;
import com.github.koraktor.steamcondenser.steam.community.GameStats;

/**
 * The Portal2Stats class represents the game statistics for a single user in
 * Portal 2
 *
 * @author Sebastian Staudt
 */
public class Portal2Stats extends GameStats {

    private Portal2Inventory inventory;

    /**
     * Creates a new object holding Portal 2 statistics for the given user
     *
     * @param steamId The custom URL or 64bit Steam ID of the user
     * @throws SteamCondenserException If an error occurs while fetching the
     *                                 stats data
     */
    public Portal2Stats(Object steamId)
            throws SteamCondenserException {
        super(steamId, "portal2");
    }

    /**
     * Returns the current Portal 2 inventory (a.k.a. Robot Enrichment) of this
     * player
     *
     * @return This player's Portal 2 backpack
     * @throws JSONException If the received data could not be parsed
     * @throws WebApiException If an error occured while querying Steam's Web
     *                         API
     */
    public Portal2Inventory getInventory()
            throws JSONException, WebApiException {
        if(!this.isPublic()) {
            return null;
        }

        if(this.inventory == null) {
            this.inventory = Portal2Inventory.create(this.steamId64);
        }

        return this.inventory;
    }

}
