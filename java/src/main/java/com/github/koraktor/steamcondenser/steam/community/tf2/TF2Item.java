/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.tf2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.koraktor.steamcondenser.exceptions.WebApiException;
import com.github.koraktor.steamcondenser.steam.community.GameItem;

/**
 * Represents a Team Fortress 2 item
 *
 * @author Sebastian Staudt
 */
public class TF2Item extends GameItem {

    private static final String[] CLASSES = {
            "scout", "sniper", "soldier", "demoman", "medic", "heavy", "pyro",
            "spy"
    };

    private Map<String, Boolean> equipped;

    /**
     * Creates a new instance of a TF2Item with the given data
     *
     * @param inventory The inventory this item is contained in
     * @param itemData The data specifying this item
     * @throws JSONException on invalid JSON data
     * @throws WebApiException on Web API errors
     */
    public TF2Item(TF2Inventory inventory, JSONObject itemData)
            throws JSONException, WebApiException {
        super(inventory, itemData);

        this.equipped = new HashMap<String, Boolean>();
        for(int classId = 0; classId < CLASSES.length; classId++) {
            this.equipped.put(CLASSES[classId], (itemData.getLong("inventory") & (1 << 16 + classId)) != 0);
        }
    }

    /**
     * Returns the class names for each class this player has equipped this
     * item
     *
     * @return The names of the classes this player has equipped this item
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
     *
     * @return <code>true</code> if the player has equipped this item at all
     */
    public boolean isEquipped() {
        return this.equipped.containsValue(true);
    }

}
