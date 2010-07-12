/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.l4d;

import org.w3c.dom.Element;

import com.github.koraktor.steamcondenser.steam.community.GameWeapon;

/**
 * @author Sebastian Staudt
 */
public class L4DExplosive extends GameWeapon {

	/**
	 * Creates a new instance of L4DExplosive based on the assigned XML data
	 *
	 * @param weaponData
	 */
	public L4DExplosive(Element weaponData) {
		super(weaponData);

		this.id = weaponData.getNodeName();
		this.shots = Integer.parseInt(weaponData.getElementsByTagName("thrown")
				.item(0).getTextContent());
	}

	/**
	 * Returns the average number of kills for one shot of this explosive.
	 */
	public float getAvgKillsPerShot() {
		return 1 / this.getAvgShotsPerKill();
	}
}
