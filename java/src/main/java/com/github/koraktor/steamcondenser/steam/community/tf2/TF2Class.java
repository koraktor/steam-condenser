/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.tf2;

import org.w3c.dom.Element;
import com.github.koraktor.steamcondenser.steam.community.GameClass;

/**
 * Represents the stats for a Team Fortress 2 class for a specific user
 * @author Sebastian Staudt
 */
public class TF2Class extends GameClass {

	protected int maxBuildingsDestroyed;
	protected int maxCaptures;
	protected int maxDamage;
	protected int maxDefenses;
	protected int maxDominations;
	protected int maxKillAssists;
	protected int maxKills;
	protected int maxRevenges;
	protected int maxScore;
	protected int maxTimeAlive;

	/**
	 * Creates a new instance of TF2Class based on the assigned XML data
	 * @param classData
	 */
	public TF2Class(Element classData) {
		this.name                  = classData.getElementsByTagName("className").item(0).getTextContent();
		this.maxBuildingsDestroyed = Integer.parseInt(classData.getElementsByTagName("ibuildingsdestroyed").item(0).getTextContent());
		this.maxCaptures           = Integer.parseInt(classData.getElementsByTagName("ipointcaptures").item(0).getTextContent());
		this.maxDamage             = Integer.parseInt(classData.getElementsByTagName("idamagedealt").item(0).getTextContent());
		this.maxDefenses           = Integer.parseInt(classData.getElementsByTagName("ipointdefenses").item(0).getTextContent());
		this.maxDominations        = Integer.parseInt(classData.getElementsByTagName("idominations").item(0).getTextContent());
		this.maxKillAssists        = Integer.parseInt(classData.getElementsByTagName("ikillassists").item(0).getTextContent());
		this.maxKills              = Integer.parseInt(classData.getElementsByTagName("inumberofkills").item(0).getTextContent());
		this.maxRevenges           = Integer.parseInt(classData.getElementsByTagName("irevenge").item(0).getTextContent());
		this.maxScore              = Integer.parseInt(classData.getElementsByTagName("ipointsscored").item(0).getTextContent());
		this.maxTimeAlive          = Integer.parseInt(classData.getElementsByTagName("iplaytime").item(0).getTextContent());
		this.playTime              = Integer.parseInt(classData.getElementsByTagName("playtimeSeconds").item(0).getTextContent());
	}

	public int getMaxBuildingsDestroyed() {
		return this.maxBuildingsDestroyed;
	}

	public int getMaxCaptures() {
		return this.maxCaptures;
	}

	public int getMaxDamage() {
		return this.maxDamage;
	}

	public int getMaxDefenses() {
		return this.maxDefenses;
	}

	public int getMaxDominations() {
		return this.maxDominations;
	}

	public int getMaxKillAssists() {
		return this.maxKillAssists;
	}

	public int getMaxKills() {
		return this.maxKills;
	}

	public int getMaxRevenges() {
		return this.maxRevenges;
	}

	public int getMaxScore() {
		return this.maxScore;
	}

	public int getMaxTimeAlive() {
		return this.maxTimeAlive;
	}
}
