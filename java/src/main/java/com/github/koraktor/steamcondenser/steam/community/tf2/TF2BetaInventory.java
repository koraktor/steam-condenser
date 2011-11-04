/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.tf2;

import java.util.HashMap;
import java.util.Map;

import com.github.koraktor.steamcondenser.exceptions.WebApiException;

/**
 * Represents the inventory (aka. Backpack) of a player of the public Team
 * Fortress 2 beta
 *
 * @author Sebastian Staudt
 */
public class TF2BetaInventory extends TF2Inventory {

    public static Map<Long, TF2BetaInventory> cache = new HashMap<Long, TF2BetaInventory>();

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
     * @throws com.github.koraktor.steamcondenser.exceptions.WebApiException on Web API errors
     */
    public static TF2BetaInventory create(long steamId64)
            throws WebApiException {
        return create(steamId64, true, false);
    }

    /**
     * This checks the cache for an existing inventory. If it exists it is
     * returned. Otherwise a new inventory is created.
     *
     * @param steamId64 The 64bit Steam ID of the user
     * @param fetchNow Whether the data should be fetched now
     * @return The inventory created from the given options
     * @throws com.github.koraktor.steamcondenser.exceptions.WebApiException on Web API errors
     */
    public static TF2BetaInventory create(long steamId64, boolean fetchNow)
            throws WebApiException {
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
     * @throws com.github.koraktor.steamcondenser.exceptions.WebApiException on Web API errors
     */
    public static TF2BetaInventory create(long steamId64, boolean fetchNow, boolean bypassCache)
            throws WebApiException {
        if(isCached(steamId64) && !bypassCache) {
            TF2BetaInventory inventory = cache.get(steamId64);
            if(fetchNow && !inventory.isFetched()) {
                inventory.fetch();
            }

            return inventory;
        } else {
            return new TF2BetaInventory(steamId64, fetchNow);
        }
    }

    /**
     * Creates a new inventory instance for the player with the given Steam ID
     * and fetches its contents
     *
     * @param steamId64 The 64bit Steam ID of the user
     * @throws com.github.koraktor.steamcondenser.exceptions.WebApiException on Web API errors
     */
    protected TF2BetaInventory(long steamId64) throws WebApiException {
        super(steamId64, true);
    }

    /**
     * Creates a new inventory instance for the player with the given Steam ID
     *
     * @param steamId64 The 64bit Steam ID of the user
     * @param fetchNow Whether the data should be fetched now
     * @throws com.github.koraktor.steamcondenser.exceptions.WebApiException on Web API errors
     */
    protected TF2BetaInventory(long steamId64, boolean fetchNow)
            throws WebApiException {
        super(steamId64, fetchNow);
    }

    /**
     * Returns the application ID of the public Team Fortress 2 beta
     *
     * @return The application ID of the public Team Fortress 2 beta is 520
     */
    protected int getAppId() {
        return 520;
    }

}
