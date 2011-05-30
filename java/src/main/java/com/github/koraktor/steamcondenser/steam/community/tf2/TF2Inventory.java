/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.tf2;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.github.koraktor.steamcondenser.exceptions.WebApiException;
import com.github.koraktor.steamcondenser.steam.community.GameInventory;
import com.github.koraktor.steamcondenser.steam.community.GameItem;

/**
 * Represents the inventory (aka. Backpack) of a Team Fortress 2 player
 *
 * @author Sebastian Staudt
 */
public class TF2Inventory extends GameInventory {

    public static Map<Long, TF2Inventory> cache = new HashMap<Long, TF2Inventory>();

    /**
     * Returns whether the requested inventory is already cached
     *
     * @param steamId64 The 64bit Steam ID of the user
     * @return Whether the inventory of the given user is already cached
     */
    public static boolean isCached(long steamId64) {
        return cache.containsKey(steamId64);
    }

    /**
     * Clears the inventory cache
     */
    public static void clearCache() {
        cache.clear();
    }

    /**
     * This checks the cache for an existing inventory. If it exists it is
     * returned. Otherwise a new inventory is created.
     *
     * @param steamId64 The 64bit Steam ID of the user
     * @return The inventory created from the given options
     * @throws JSONException on invalid JSON data
     * @throws WebApiException on Web API errors
     */
    public static TF2Inventory create(long steamId64)
            throws JSONException, WebApiException {
        return create(steamId64, true, false);
    }

    /**
     * This checks the cache for an existing inventory. If it exists it is
     * returned. Otherwise a new inventory is created.
     *
     * @param steamId64 The 64bit Steam ID of the user
     * @param fetchNow Whether the data should be fetched now
     * @return The inventory created from the given options
     * @throws JSONException on invalid JSON data
     * @throws WebApiException on Web API errors
     */
    public static TF2Inventory create(long steamId64, boolean fetchNow)
            throws JSONException, WebApiException {
        return create(steamId64, fetchNow, false);
    }

    /**
     * This checks the cache for an existing inventory. If it exists it is
     * returned. Otherwise a new inventory is created.
     *
     * @param steamId64 The 64bit Steam ID of the user
     * @param fetchNow Whether the data should be fetched now
     * @param bypassCache Whether the cache should be bypassed
     * @return The inventory created from the given options
     * @throws JSONException on invalid JSON data
     * @throws WebApiException on Web API errors
     */
    public static TF2Inventory create(long steamId64, boolean fetchNow, boolean bypassCache)
            throws JSONException, WebApiException {
        if(isCached(steamId64) && !bypassCache) {
            TF2Inventory inventory = cache.get(steamId64);
            if(fetchNow && !inventory.isFetched()) {
                inventory.fetch();
            }

            return inventory;
        } else {
            return new TF2Inventory(steamId64, fetchNow);
        }
    }

    /**
     * Creates a new inventory instance for the player with the given Steam ID
     * and fetches its contents
     *
     * @param steamId64 The 64bit Steam ID of the user
     * @throws JSONException on invalid JSON data
     */
    protected TF2Inventory(long steamId64)
            throws JSONException, WebApiException {
        super(steamId64, true);
    }

    /**
     * Creates a new inventory instance for the player with the given Steam ID
     *
     * @param steamId64 The 64bit Steam ID of the user
     * @param fetchNow Whether the data should be fetched now
     * @throws JSONException on invalid JSON data
     */
    protected TF2Inventory(long steamId64, boolean fetchNow)
            throws JSONException, WebApiException {
        super(steamId64, fetchNow);
    }

    /**
     * Returns the application ID of Team Fortress 2
     *
     * @return The application ID of Team Fortress 2 is 440
     */
    protected int getAppId() {
        return 440;
    }

    /**
     * Returns the item class for Team Fortress 2
     *
     * @return The item class for Team Fortress 2 is TF2Item
     * @see TF2Item
     */
    protected Class<? extends GameItem> getItemClass() {
        return TF2Item.class;
    }

}
