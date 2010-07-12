/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.dods;

import org.w3c.dom.Element;

import com.github.koraktor.steamcondenser.steam.community.GameClass;

/**
 * Represents the stats for a Day of Defeat: Source class for a specific user
 *
 * @author Sebastian Staudt
 */
public class DoDSClass extends GameClass {

	private int blocks;

	private int bombsDefused;

	private int bombsPlanted;

	private int captures;

	private int deaths;

	private int dominations;

	private String key;

	private int kills;

	private int roundsLost;

	private int roundsWon;

	private int revenges;

	/**
	 * Creates a new instance of DoDSClass based on the assigned XML data
	 *
	 * @param classData
	 */
	public DoDSClass(Element classData) {
		this.blocks = Integer.parseInt(classData.getElementsByTagName("blocks")
				.item(0).getTextContent());
		this.bombsDefused = Integer.parseInt(classData.getElementsByTagName(
				"bombsdefused").item(0).getTextContent());
		this.bombsPlanted = Integer.parseInt(classData.getElementsByTagName(
				"bombsplanted").item(0).getTextContent());
		this.captures = Integer.parseInt(classData.getElementsByTagName(
				"captures").item(0).getTextContent());
		this.deaths = Integer.parseInt(classData.getElementsByTagName("deaths")
				.item(0).getTextContent());
		this.dominations = Integer.parseInt(classData.getElementsByTagName(
				"dominations").item(0).getTextContent());
		this.key = classData.getAttribute("key");
		this.kills = Integer.parseInt(classData.getElementsByTagName("kills")
				.item(0).getTextContent());
		this.name = classData.getElementsByTagName("name").item(0)
				.getTextContent();
		this.playTime = Integer.parseInt(classData.getElementsByTagName(
				"playtime").item(0).getTextContent());
		this.roundsLost = Integer.parseInt(classData.getElementsByTagName(
				"roundslost").item(0).getTextContent());
		this.roundsWon = Integer.parseInt(classData.getElementsByTagName(
				"roundswon").item(0).getTextContent());
		this.revenges = Integer.parseInt(classData.getElementsByTagName(
				"revenges").item(0).getTextContent());
	}

	public int getBlocks() {
		return this.blocks;
	}

	public int getBombsDefuse() {
		return this.bombsDefused;
	}

	public int getBombsPlanted() {
		return this.bombsPlanted;
	}

	public int getCaptures() {
		return this.captures;
	}

	public int getDeaths() {
		return this.deaths;
	}

	public int getDominations() {
		return this.dominations;
	}

	public String getKey() {
		return this.key;
	}

	public int getKills() {
		return this.kills;
	}

	public int getRoundsLost() {
		return this.roundsLost;
	}

	public int getRoundsWon() {
		return this.roundsWon;
	}

	public int getRevenges() {
		return this.revenges;
	}
}
