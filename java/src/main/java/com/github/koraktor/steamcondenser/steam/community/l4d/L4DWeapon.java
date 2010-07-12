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
public class L4DWeapon extends AbtractL4DWeapon {

	/**
	 * Creates a new instance of L4DWeapon based on the assigned XML data
	 *
	 * @param weaponData
	 */
	public L4DWeapon(Element weaponData) {
		super(weaponData);

		this.killPercentage = weaponData.getElementsByTagName("killpct")
				.item(0).getTextContent();
	}

}
