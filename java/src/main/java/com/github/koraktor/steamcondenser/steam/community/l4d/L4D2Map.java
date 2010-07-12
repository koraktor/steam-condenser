/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.l4d;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.SteamId;

public class L4D2Map extends L4DMap {

    private static final String[] INFECTED = { "boomer", "charger", "common", "hunter", "jockey", "smoker", "spitter", "tank" };

    private static final String[] ITEMS = { "adrenaline", "defibs", "medkits", "pills"};

    private HashMap<String, Integer> items;

    private HashMap<String, Integer> kills;

    private boolean played;

    private ArrayList<SteamId> teammates;

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

    public HashMap<String, Integer> getItems() {
        return this.items;
    }

    public HashMap<String, Integer> getKills() {
        return this.kills;
    }

    public ArrayList<SteamId> getTeammates() {
        return this.teammates;
    }

    public boolean hasPlayed() {
        return this.played;
    }
}
