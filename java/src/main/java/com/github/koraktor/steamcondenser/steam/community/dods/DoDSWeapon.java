/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.dods;

import org.w3c.dom.Element;

import com.github.koraktor.steamcondenser.steam.community.GameWeapon;

/**
 * Represents the stats for a Day of Defeat: Source weapon for a specific user
 *
 * @author Sebastian Staudt
 */
public class DoDSWeapon extends GameWeapon {

	private float avgHitsPerKill;

	private float headshotPercentage;

	private int headshots;

	private int hits;

	private float hitPercentage;

	private String name;

	public DoDSWeapon(Element weaponData) {
		super(weaponData);

		this.headshots = Integer.parseInt(weaponData.getElementsByTagName(
				"headshots").item(0).getTextContent());
		this.id = weaponData.getAttribute("key");
		this.name = weaponData.getElementsByTagName("name").item(0)
				.getTextContent();
		this.shots = Integer.parseInt(weaponData.getElementsByTagName(
				"shotsfired").item(0).getTextContent());
		this.hits = Integer.parseInt(weaponData
				.getElementsByTagName("shotshit").item(0).getTextContent());
	}

	/**
	 * Returns the average number of hits needed for a kill with this weapon.
	 * Calculates the value if needed.
	 */
	public float getAvgHitsPerKill() {
		if (this.avgHitsPerKill == 0) {
			this.avgHitsPerKill = this.hits / this.kills;
		}

		return this.avgHitsPerKill;
	}

	/**
	 * Returns the percentage of headshots relative to the shots hit with this
	 * weapon. Calculates the value if needed.
	 */
	public float getHeadshotPercentage() {
		if (this.headshotPercentage == 0) {
			this.headshotPercentage = this.headshots / this.hits;
		}

		return this.headshotPercentage;
	}

	public int getHeadshots() {
		return this.headshots;
	}

	/**
	 * Returns the percentage of hits relative to the shots fired with this
	 * weapon. Calculates the value if needed.
	 */
	public float getHitPercentage() {
		if (this.hitPercentage == 0) {
			this.hitPercentage = this.hits / this.shots;
		}

		return this.hitPercentage;
	}

	public String getName() {
		return this.name;
	}

	public int getHits() {
		return this.hits;
	}

}
