/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.koraktor.steamcondenser.exceptions.WebApiException;

/**
 * Provides basic functionality to represent an inventory of player in a game
 *
 * @author Sebastian Staudt
 */
public abstract class GameInventory {

    private static Map<Integer, Map<String, JSONObject>> attributeSchemas = new HashMap<Integer, Map<String, JSONObject>>();

    private static Map<Integer, Map<Integer, JSONObject>> itemSchemas = new HashMap<Integer, Map<Integer, JSONObject>>();

    private static Map<Integer, Map<Integer, String>> qualitySchemas = new HashMap<Integer, Map<Integer, String>>();

    private static String schemaLanguage = "en";

    private Date fetchDate;

    private Map<Integer, GameItem> items;

    private long steamId64;

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
    public GameInventory(long steamId64, boolean fetchNow)
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
    @SuppressWarnings("unchecked")
    protected void cache() {
        try {
            Object cache = this.getClass().getField("cache").get(null);
            ((Map<Long, GameInventory>) cache).put(this.steamId64, this);
        } catch(IllegalAccessException e) {
        } catch(NoSuchFieldException e) {}
    }

    /**
     * Updates the contents of the backpack using Steam Web API
     *
     * @throws JSONException on invalid JSON data
     * @throws WebApiException on Web API errors
     */
    @SuppressWarnings("unchecked")
    public void fetch()
            throws JSONException, WebApiException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("SteamID", this.steamId64);
        JSONObject result = WebApi.getJSONData("IEconItems_" + this.getAppId(), "GetPlayerItems", 1, params);

        this.items = new HashMap<Integer, GameItem>();
        JSONArray itemsData = result.getJSONArray("items");
        for(int i = 0; i < itemsData.length(); i ++) {
            JSONObject itemData = itemsData.getJSONObject(i);
            if(itemData != null) {
                try {
                    GameItem item = this.getItemClass().getConstructor(this.getClass(), JSONObject.class).newInstance(this, itemData);
                    this.items.put(item.getBackpackPosition() - 1, item);
                } catch(IllegalAccessException e) {
                } catch(InstantiationException e) {
                } catch(InvocationTargetException e) {
                } catch(NoSuchMethodException e) {}
            }
        }

        this.fetchDate = new Date();
    }

    /**
     * Returns the application ID of the game this inventory belongs to
     *
     * @return The application ID of the game this inventory belongs to
     */
    protected abstract int getAppId();

    /**
     * Returns the 64bit SteamID of the player owning this inventory
     *
     * @return The 64bit SteamID
     */
    public long getSteamId64() {
        return this.steamId64;
    }

    /**
     * Returns the attribute schema
     *
     * The attribute schema is fetched first if not done already
     *
     * @return The attribute schema for the game this inventory belongs to
     * @throws JSONException on invalid JSON data
     * @throws WebApiException on Web API errors
     */
    public Map<String, JSONObject> getAttributeSchema()
            throws JSONException, WebApiException {
        if(!attributeSchemas.containsKey(this.getAppId())) {
            this.updateSchema();
        }

        return attributeSchemas.get(this.getAppId());
    }

    /**
     * Returns the item at the given position in the backpack. The positions
     * range from 1 to 100 instead of the usual array indices (0 to 99).
     *
     * @param index The position of the item in the backpack
     * @return The item at the given position
     */
    public GameItem getItem(int index) {
        return this.items.get(index - 1);
    }

    /**
     * Returns the item class for the game this inventory belongs to
     *
     * @return The item class for the game this inventory belongs to
     * @see GameItem
     */
    protected abstract Class<? extends GameItem> getItemClass();

    /**
     * Returns the item schema
     *
     * The item schema is fetched first if not done already
     *
     * @return The item schema for the game this inventory belongs to
     * @throws JSONException on invalid JSON data
     * @throws WebApiException on Web API errors
     */
    public Map<Integer, JSONObject> getItemSchema()
            throws JSONException, WebApiException {
        if(!itemSchemas.containsKey(this.getAppId())) {
            this.updateSchema();
        }

        return itemSchemas.get(this.getAppId());
    }

    /**
     * Returns an array of all items in this players inventory.
     *
     * @return All items in the backpack
     */
    public Map<Integer, GameItem> getItems() {
        return this.items;
    }

    /**
     * Returns the item quality schema
     *
     * The item schema is fetched first if not done already
     *
     * @return The item quality schema for the game this inventory belongs to
     * @throws JSONException on invalid JSON data
     * @throws WebApiException on Web API errors
     */
    public Map<Integer, String> getQualitySchema()
            throws JSONException, WebApiException {
        if(!qualitySchemas.containsKey(this.getAppId())) {
            this.updateSchema();
        }

        return qualitySchemas.get(this.getAppId());
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
     * Sets the language the schema should be fetched in (default is: +'en'+)
     *
     * @param language The language code for the language item descriptions
     *        should be fetched in
     */
    public static void setSchemaLanguage(String language) {
        schemaLanguage = language;
    }

    /**
     * Returns the number of items in the user's backpack
     *
     * @return The number of items in the backpack
     */
    public int size() {
        return this.items.size();
    }

    /**
     * Updates the item schema (this includes attributes and qualities) using
     * the "GetSchema" method of interface "IEconItems_{AppId}"
     *
     * @throws JSONException on invalid JSON data
     * @throws WebApiException on Web API errors
     */
    protected void updateSchema()
            throws JSONException, WebApiException {
        Map<String, Object> params = new HashMap<String, Object>();
        if(schemaLanguage != null) {
            params.put("language", schemaLanguage);
        }
        JSONObject result = WebApi.getJSONData("IEconItems_" + this.getAppId(), "GetSchema", 1, params);

        Map<String, JSONObject> attributeSchema = new HashMap<String, JSONObject>();
        attributeSchemas.put(this.getAppId(), attributeSchema);
        JSONArray attributesData = result.getJSONArray("attributes");
        for(int i = 0; i < attributesData.length(); i++) {
            JSONObject attributeData = attributesData.getJSONObject(i);
            attributeSchema.put(attributeData.getString("name"), attributeData);
        }

        Map<Integer, JSONObject> itemSchema = new HashMap<Integer, JSONObject>();
        itemSchemas.put(this.getAppId(), itemSchema);
        JSONArray itemsData = result.getJSONArray("items");
        for(int i = 0; i < itemsData.length(); i++) {
            JSONObject itemData = itemsData.getJSONObject(i);
            itemSchema.put(itemData.getInt("defindex"), itemData);
        }

        Map<Integer, String> qualitySchema = new HashMap<Integer, String>();
        qualitySchemas.put(this.getAppId(), qualitySchema);
        JSONObject qualitiesData = result.getJSONObject("qualities");
        Iterator qualityIterator = qualitiesData.keys();
        while(qualityIterator.hasNext()) {
            String quality = (String) qualityIterator.next();
            qualitySchema.put(qualitiesData.getInt(quality), quality);
        }
    }

}
