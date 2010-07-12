/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009-2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.l4d;

import org.w3c.dom.Element;

/**
 * @author Sebastian Staudt
 */
public class L4DMap {
	public static int GOLD   = 1;
	public static int SILVER = 2;
	public static int BRONZE = 3;
	public static int NONE   = 0;

	protected float bestTime;

	protected String id;

	protected int medal;

	protected String name;

	private int timesPlayed;

    public L4DMap() {}

	public L4DMap(Element mapData) {
		this.bestTime    = Float.parseFloat(mapData.getElementsByTagName("besttimeseconds").item(0).getTextContent());
        this.id          = mapData.getNodeName();
        this.name        = mapData.getElementsByTagName("name").item(0).getTextContent();
        this.timesPlayed = Integer.parseInt(mapData.getElementsByTagName("timesplayed").item(0).getTextContent());

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

	public float getBestTime() {
		return this.bestTime;
	}

	public String getId() {
		return this.id;
	}

	public int getMedal() {
		return this.medal;
	}

	public String getName() {
		return this.name;
	}

	public int getTimesPlayed() {
		return this.timesPlayed;
	}
}
