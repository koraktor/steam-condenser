/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.portal2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.koraktor.steamcondenser.exceptions.WebApiException;
import com.github.koraktor.steamcondenser.steam.community.GameItem;

/**
 * Represents a Portal 2 item
 *
 * @author Sebastian Staudt
 */
public class Portal2Item extends GameItem {

    private static final String[] BOTS = { "pbody", "atlas" };

    private Map<String, Boolean> equipped;

    /**
     * Creates a new instance of a Portal2Item with the given data
     *
     * @param inventory The inventory this item is contained in
     * @param itemData The data specifying this item
     * @throws JSONException on invalid JSON data
     * @throws WebApiException on Web API errors
     */
    public Portal2Item(Portal2Inventory inventory, JSONObject itemData)
            throws JSONException, WebApiException {
        super(inventory, itemData);

        this.equipped = new HashMap<String, Boolean>();
        for(int botId = 0; botId < BOTS.length; botId++) {
            this.equipped.put(BOTS[botId], (itemData.getLong("inventory") & (1 << 16 + botId)) != 0);
        }
    }

    /**
     * Returns the name for each bot this player has equipped this item
     *
     * @return The names of the bots this player has equipped this item
     */
    public List<String> getBotsEquipped() {
        List<String> botsEquipped = new ArrayList<String>();
        for(Map.Entry<String, Boolean> botEquipped : this.equipped.entrySet()) {
            if(botEquipped.getValue()) {
                botsEquipped.add(botEquipped.getKey());
            }
        }

        return botsEquipped;
    }

    /**
     * Returns whether this item is equipped by this player at all
     *
     * @return Whether this item is equipped by this player at all
     */
    public boolean isEquipped() {
        return this.equipped.containsValue(true);
    }

}
