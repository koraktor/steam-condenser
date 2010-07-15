/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.tf2;

import java.util.*;

import com.github.koraktor.steamcondenser.exceptions.WebApiException;
import com.github.koraktor.steamcondenser.steam.community.WebApi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents a Team Fortress 2 item
 *
 * @author Sebastian Staudt
 */
public class TF2Item {

    private static final String[] CLASSES = {
            "scout", "sniper", "soldier", "demoman", "medic", "heavy", "pyro",
            "spy"
    };

    private static Map<String, JSONObject> attributeSchema = null;

    private static Map<Integer, JSONObject> itemSchema = null;

    private static Map<Integer, String> qualities;

    private static String schemaLanguage = "en";

    private JSONArray attributes;

    private int backpackPosition;

    private String className;

    private int count;

    private int defindex;

    private Map<String, Boolean> equipped;

    private int id;

    private int level;

    private String name;

    private String quality;

    private String slot;

    private String type;

    /**
     * Returns the attribute schema
     *
     * The attribute schema is fetched first if not done already
     */
    public static Map<String, JSONObject> getAttributeSchema()
            throws JSONException, WebApiException {
        if(attributeSchema == null) {
            updateSchema();
        }

        return attributeSchema;
    }

    /**
     * Returns the item schema
     *
     * The item schema is fetched first if not done already
     */
    public static Map<Integer, JSONObject> getItemSchema()
            throws JSONException, WebApiException {
        if(itemSchema == null) {
            updateSchema();
        }

        return itemSchema;
    }

    /**
     * Sets the language the schema should be fetched in (default is: +'en'+)
     */
    public static void setSchemaLanguage(String language) {
        schemaLanguage = language;
    }

  /**
   * Creates a new instance of a TF2Item with the given data
   */
    public TF2Item(JSONObject itemData)
            throws JSONException, WebApiException {
        if(itemSchema == null) {
            updateSchema();
        }

        this.defindex         = itemData.getInt("defindex");
        this.backpackPosition = (int) itemData.getLong("inventory") & 0xffff;
        this.className        = itemSchema.get(this.defindex).getString("item_class");
        this.count            = itemData.getInt("quantity");
        this.id               = itemData.getInt("id");
        this.level            = itemData.getInt("level");
        this.name             = itemSchema.get(this.defindex).getString("item_name");
        this.quality          = qualities.get(itemData.getInt("quality"));
        this.slot             = itemSchema.get(this.defindex).getString("item_slot");
        this.type             = itemSchema.get(this.defindex).getString("item_type_name");

        this.equipped = new HashMap<String, Boolean>();
        for(int classId = 0; classId < CLASSES.length; classId++) {
            this.equipped.put(CLASSES[classId], (itemData.getLong("inventory") & (1 << 16 + classId)) != 0);
        }

        if(!itemSchema.get(this.defindex).isNull("attributes")) {
          this.attributes = itemSchema.get(this.defindex).getJSONObject("attributes").getJSONArray("attribute");
        }
    }

    public JSONArray getAttributes() {
        return this.attributes;
    }

    public int getBackpackPosition() {
        return this.backpackPosition;
    }

    public String getClassName() {
        return this.className;
    }

    public int getCount() {
        return this.count;
    }

    public int getDefIndex() {
        return this.defindex;
    }

    public int getId() {
        return this.id;
    }

    public int getLevel() {
        return this.level;
    }

    public String getName() {
        return this.name;
    }

    public String getQuality() {
        return this.quality;
    }

    public String getSlot() {
        return this.slot;
    }

    public String getType() {
        return this.type;
    }

    /**
     * Returns the class symbols for each class this player has equipped this item
     */
    public List<String> getClassesEquipped() {
        List<String> classesEquipped = new ArrayList<String>();
        for(Map.Entry<String, Boolean> classEquipped : this.equipped.entrySet()) {
            if(classEquipped.getValue()) {
                classesEquipped.add(classEquipped.getKey());
            }
        }

        return classesEquipped;
    }

    /**
     * Returns whether this item is equipped by this player at all
     */
    public boolean isEquipped() {
        return this.equipped.containsValue(true);
    }

    /**
     * Updates the item schema (this includes attributes and qualities) using
     * the "GetSchema" method of interface "ITFItems_440"
     */
    protected static void updateSchema()
            throws JSONException, WebApiException {
        Map<String, Object> params = new HashMap<String, Object>();
        if(schemaLanguage != null) {
            params.put("language", schemaLanguage);
        }
        JSONObject result = WebApi.getJSONData("ITFItems_440", "GetSchema", 1, params);

        attributeSchema = new HashMap<String, JSONObject>();
        JSONArray attributesData = result.getJSONObject("attributes").getJSONArray("attribute");
        for(int i = 0; i < attributesData.length(); i++) {
            JSONObject attributeData = attributesData.getJSONObject(i);
            attributeSchema.put(attributeData.getString("name"), attributeData);
        }

        itemSchema = new HashMap<Integer, JSONObject>();
        JSONArray itemsData = result.getJSONObject("items").getJSONArray("item");
        for(int i = 0; i < itemsData.length(); i++) {
            JSONObject itemData = itemsData.getJSONObject(i);
            itemSchema.put(itemData.getInt("defindex"), itemData);
        }

        qualities = new HashMap<Integer, String>();
        JSONObject qualitiesData = result.getJSONObject("qualities");
        Iterator qualityIterator = qualitiesData.keys();
        while(qualityIterator.hasNext()) {
            String quality = (String) qualityIterator.next();
            qualities.put(qualitiesData.getInt(quality), quality);
        }
    }
}
