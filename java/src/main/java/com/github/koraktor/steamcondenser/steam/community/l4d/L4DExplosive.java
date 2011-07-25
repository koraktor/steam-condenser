/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.l4d;

import org.w3c.dom.Element;

import com.github.koraktor.steamcondenser.steam.community.GameWeapon;

/**
 * This class represents the statistics of a single explosive weapon for a user
 * in Left4Dead
 *
 * @author Sebastian Staudt
 */
public class L4DExplosive extends GameWeapon {

	/**
     * Creates a new instance of an explosivve based on the given XML data
     *
     * @param weaponData The XML data of this explosive
	 */
	public L4DExplosive(Element weaponData) {
		super(weaponData);

		this.id = weaponData.getNodeName();
		this.shots = Integer.parseInt(weaponData.getElementsByTagName("thrown")
				.item(0).getTextContent());
	}

	/**
     * Returns the average number of killed zombies for one shot of this
     * explosive
     *
     * @return The average number of kills per shot
	 */
	public float getAvgKillsPerShot() {
		return 1 / this.getAvgShotsPerKill();
	}
}
