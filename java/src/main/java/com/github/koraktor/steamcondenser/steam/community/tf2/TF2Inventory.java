/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.tf2;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.koraktor.steamcondenser.exceptions.WebApiException;
import com.github.koraktor.steamcondenser.steam.community.WebApi;

/**
 * Represents the inventory (aka. Backpack) of a Team Fortress 2 player
 *
 * @author Sebastian Staudt
 */
public class TF2Inventory {

    private static Map<Long, TF2Inventory> cache = new HashMap<Long, TF2Inventory>();

    private Date fetchDate;

    private Map<Integer, TF2Item> items;

    private long steamId64;

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
     * @return TF2Inventory The inventory created from the given options
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
     * @return TF2Inventory The inventory created from the given options
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
     * @return TF2Inventory The inventory created from the given options
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
     * Creates a new inventory object for the given SteamID64. This calls
     * fetch() to update the data and create the TF2Item instances contained in
     * this players backpack
     *
     * @param steamId64 The 64bit Steam ID of the user
     * @throws JSONException on invalid JSON data
     * @throws WebApiException on Web API errors
     */
    public TF2Inventory(long steamId64)
            throws JSONException, WebApiException {
        this(steamId64, true);
    }

    /**
     * Creates a new inventory object for the given SteamID64. This calls
     * fetch() to update the data and create the TF2Item instances contained in
     * this players backpack
     *
     * @param steamId64 The 64bit Steam ID of the user
     * @param fetchNow Whether the data should be fetched now
     * @throws JSONException on invalid JSON data
     * @throws WebApiException on Web API errors
     */
    public TF2Inventory(long steamId64, boolean fetchNow)
            throws JSONException, WebApiException {
        this.steamId64 = steamId64;

        if(fetchNow) {
            this.fetch();
        }

        this.cache();
    }

    /**
     * Saves this inventory in the cache
     */
    public void cache() {
        if(!cache.containsKey(this.steamId64)) {
            cache.put(this.steamId64, this);
        }
    }

    /**
     * Returns the item at the given position in the backpack. The positions
     * range from 1 to 100 instead of the usual array indices (0 to 99).
     *
     * @param index The position of the item in the backpack
     * @return The item at the given position
     */
    public TF2Item getItem(int index) {
        return this.items.get(index - 1);
    }

    /**
     * Returns an array of all items in this players inventory.
     *
     * @return All items in the backpack
     */
    public Map<Integer, TF2Item> getItems() {
        return this.items;
    }

    /**
     * Returns the 64bit SteamID of the player owning this inventory
     *
     * @return The 64bit SteamID
     */
    public long getSteamId64() {
        return this.steamId64;
    }

    /**
     * Updates the contents of the backpack using Steam Web API
     *
     * @throws JSONException on invalid JSON data
     * @throws WebApiException on Web API errors
     */
    public void fetch()
            throws JSONException, WebApiException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("SteamID", this.steamId64);
        JSONObject result = WebApi.getJSONData("ITFItems_440", "GetPlayerItems", 1, params);

        this.items = new HashMap<Integer, TF2Item>();
        JSONArray itemsData = result.getJSONObject("items").getJSONArray("item");
        for(int i = 0; i < itemsData.length(); i ++) {
            JSONObject itemData = itemsData.getJSONObject(i);
            if(itemData != null) {
                TF2Item item = new TF2Item(itemData);
                this.items.put(item.getBackpackPosition() - 1, item);
            }
        }

        this.fetchDate = new Date();
    }

    /**
     * Returns whether the items contained in this inventory have been already
     * fetched
     *
     * @return Whether the contents backpack have been fetched
     */
    public boolean isFetched() {
        return this.fetchDate != null;
    }

    /**
     * Returns the number of items in the user's backpack
     *
     * @return The number of items in the backpack
     */
    public int size() {
        return this.items.size();
    }

}
