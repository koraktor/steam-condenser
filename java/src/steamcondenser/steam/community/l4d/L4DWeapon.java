/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009, Sebastian Staudt
 */

package steamcondenser.steam.community.l4d;

import org.w3c.dom.Element;

import steamcondenser.steam.community.GameWeapon;

/**
 * @author Sebastian Staudt
 */
public class L4DWeapon extends GameWeapon {
	private String accuracy;

	private String headshotsPercentage;

	private String killPercentage;

	/**
	 * Creates a new instance of L4DWeapon based on the assigned XML data
	 * 
	 * @param weaponData
	 */
	public L4DWeapon(Element weaponData) {
		super(weaponData);

		this.accuracy = weaponData.getElementsByTagName("accuracy").item(0)
				.getTextContent();
		this.headshotsPercentage = weaponData.getElementsByTagName("headshots")
				.item(0).getTextContent();
		this.id = weaponData.getNodeName();
		this.killPercentage = weaponData.getElementsByTagName("killpct")
				.item(0).getTextContent();
		this.shots = Integer.parseInt(weaponData.getElementsByTagName("shots")
				.item(0).getTextContent());
	}

	public String getAccuracy() {
		return this.accuracy;
	}

	public String getHeadshotsPercentage() {
		return this.headshotsPercentage;
	}

	public String getKillPercentage() {
		return this.killPercentage;
	}
}
