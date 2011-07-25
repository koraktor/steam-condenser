/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.l4d;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.SteamId;

/**
 * This class holds statistical information about a map played by a player in
 * Survival mode of Left4Dead 2
 * <p>
 * The basic information provided is more or less the same for Left4Dead and
 * Left4Dead 2, but parsing has to be done differently.
 *
 * @author Sebastian Staudt
 */
public class L4D2Map extends L4DMap {

    private static final String[] INFECTED = { "boomer", "charger", "common", "hunter", "jockey", "smoker", "spitter", "tank" };

    private static final String[] ITEMS = { "adrenaline", "defibs", "medkits", "pills"};

    private HashMap<String, Integer> items;

    private HashMap<String, Integer> kills;

    private boolean played;

    private ArrayList<SteamId> teammates;

    /**
     * Creates a new instance of a map based on the given XML data
     * <p>
     * The map statistics for the Survival mode of Left4Dead 2 hold much more
     * information than those for Left4Dead, e.g. the teammates and items are
     * listed.
     *
     * @param mapData The XML data for this map
     */
    public L4D2Map(Element mapData)
            throws SteamCondenserException {
        String imgUrl = mapData.getElementsByTagName("img").item(0).getTextContent();
        this.id = imgUrl.substring(imgUrl.lastIndexOf('/'), -4);
        this.name = mapData.getElementsByTagName("name").item(0).getTextContent();
        this.played = mapData.getElementsByTagName("hasPlayed").item(0).getTextContent().equals("1");

        if(this.played) {
            this.bestTime = Float.parseFloat(mapData.getElementsByTagName("besttimemilliseconds").item(0).getTextContent()) / 1000;

            this.items = new HashMap<String, Integer>();
            for(String item : ITEMS) {
                this.items.put(item, Integer.parseInt(mapData.getElementsByTagName("items_" + item).item(0).getTextContent()));
            }

            this.kills = new HashMap<String, Integer>();
            for(String infected : INFECTED) {
                this.items.put(infected, Integer.parseInt(mapData.getElementsByTagName("kills_" + infected).item(0).getTextContent()));
            }

            this.teammates = new ArrayList<SteamId>();
            NodeList teammateNodes = mapData.getElementsByTagName("teammates").item(0).getChildNodes();
            for(int i = 0; i < teammateNodes.getLength(); i++) {
                Element teammateNode = (Element) teammateNodes.item(i);
                this.teammates.add(SteamId.create(Long.parseLong(teammateNode.getTextContent())));
            }

            String medal = mapData.getElementsByTagName("medal").item(0).getTextContent();
            if(medal.equals("gold")) {
                this.medal = GOLD;
            } else if(medal.equals("silver")) {
                this.medal = SILVER;
            } else if(medal.equals("bronze")) {
                this.medal = BRONZE;
            } else {
                this.medal = NONE;
            }
        }
    }

    /**
     * Returns statistics about the items used by the player on this map
     *
     * @return array The items used by the player
     */
    public HashMap<String, Integer> getItems() {
        return this.items;
    }

    /**
     * Returns the number of special infected killed by the player grouped by
     * the names of the special infected
     *
     * @return array The special infected killed by the player
     */
    public HashMap<String, Integer> getKills() {
        return this.kills;
    }

    /**
     * Returns the SteamIDs of the teammates of the player in his best game on
     * this map
     *
     * @return array The SteamIDs of the teammates in the best game
     */
    public ArrayList<SteamId> getTeammates() {
        return this.teammates;
    }

    /**
     * Returns whether the player has already played this map
     *
     * @return bool <code>true</code> if the player has already played this map
     */
    public boolean hasPlayed() {
        return this.played;
    }
}
