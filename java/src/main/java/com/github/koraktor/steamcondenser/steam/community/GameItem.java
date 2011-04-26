/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.koraktor.steamcondenser.exceptions.WebApiException;

/**
 * Provides basic functionality to represent an item in a game
 *
 * @author Sebastian Staudt
 */
public class GameItem {

    private JSONArray attributes;

    private int backpackPosition;

    private String className;

    private int count;

    private int defindex;

    private int id;

    private int level;

    private String name;

    private String quality;

    private String slot;

    private boolean tradeable;

    private String type;

    /**
     * Creates a new instance of a GameItem with the given data
     *
     * @param inventory The inventory this item is contained in
     * @param itemData The data specifying this item
     * @throws JSONException on invalid JSON data
     * @throws WebApiException on Web API errors
     */
    public GameItem(GameInventory inventory, JSONObject itemData)
            throws JSONException, WebApiException {
        this.defindex         = itemData.getInt("defindex");
        this.backpackPosition = (int) itemData.getLong("inventory") & 0xffff;
        this.className        = inventory.getItemSchema().get(this.defindex).getString("item_class");
        this.count            = itemData.getInt("quantity");
        this.id               = itemData.getInt("id");
        this.level            = itemData.getInt("level");
        this.name             = inventory.getItemSchema().get(this.defindex).getString("item_name");
        this.quality          = inventory.getQualitySchema().get(itemData.getInt("quality"));
        this.slot             = inventory.getItemSchema().get(this.defindex).getString("item_slot");
        this.tradeable        = itemData.isNull("flag_cannot_trade") || !itemData.getBoolean("flag_cannot_trade");
        this.type             = inventory.getItemSchema().get(this.defindex).getString("item_type_name");

        if(!inventory.getItemSchema().get(this.defindex).isNull("attributes")) {
          this.attributes = inventory.getItemSchema().get(this.defindex).getJSONArray("attributes");
        }
    }

    /**
     * Return the attributes of this item
     *
     * @return The attributes of this item
     */
    public JSONArray getAttributes() {
        return this.attributes;
    }

    /**
     * Returns the position of this item in the player's inventory
     *
     * @return The position of this item in the player's inventory
     */
    public int getBackpackPosition() {
        return this.backpackPosition;
    }

    /**
     * Returns the class of this item
     *
     * @return The class of this item
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * Returns the number of items the player owns of this item
     *
     * @return The quanitity of this item
     */
    public int getCount() {
        return this.count;
    }

    /**
     * Returns the index where the item is defined in the schema
     *
     * @return The schema index of this item
     */
    public int getDefIndex() {
        return this.defindex;
    }

    /**
     * Returns the ID of this item
     *
     * @return The ID of this item
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the level of this item
     *
     * @return The level of this item
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Returns the level of this item
     *
     * @return The level of this item
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the quality of this item
     *
     * @return The quality of this item
     */
    public String getQuality() {
        return this.quality;
    }

    /**
     * Returns the slot where this item can be equipped in
     *
     * @return The slot where this item can be equipped in
     */
    public String getSlot() {
        return this.slot;
    }

    /**
     * Returns whether this item is tradeable
     *
     * @return bool Whether this item is tradeable
     */
    public boolean isTradeable() {
        return this.tradeable;
    }

    /**
     * Returns the type of this item
     *
     * @return The type of this item
     */
    public String getType() {
        return this.type;
    }

}
