/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.l4d;

import org.w3c.dom.Element;
import com.github.koraktor.steamcondenser.steam.community.GameWeapon;

/**
 * This abstract class is a base class for weapons in Left4Dead and Left4Dead 2
 * as the weapon stats for both games are very similar
 *
 * @author Sebastian Staudt
 */
public abstract class AbtractL4DWeapon extends GameWeapon {

    protected String accuracy;

    protected String headshotPercentage;

    protected String killPercentage;

    protected String name;

    protected int shots;

    /**
     * Creates a new instance of weapon from the given XML data and parses
     * common data for both, <code>L4DWeapon</code> and <code>L4D2Weapon</code>
     *
     * @param SimpleXMLElement $weaponData The XML data for this weapon
     */
    public AbtractL4DWeapon(Element weaponData) {
        super(weaponData);

        this.accuracy = weaponData.getElementsByTagName("accuracy").item(0)
                .getTextContent();
        this.headshotPercentage = weaponData.getElementsByTagName("headshots")
                .item(0).getTextContent();
        this.name = weaponData.getTagName();
        this.shots = Integer.valueOf(weaponData.getElementsByTagName("shots")
                .item(0).getTextContent());
    }

    /**
     * Returns the overall accuracy of the player with this weapon
     *
     * @return string The accuracy of the player with this weapon
     */
    public String getAccuracy() {
        return this.accuracy;
    }

    /**
     * Returns the percentage of kills with this weapon that have been
     * headshots
     *
     * @return string The percentage of headshots with this weapon
     */
    public String getHeadshotPercentage() {
        return this.headshotPercentage;
    }

    /**
     * Returns the percentage of overall kills of the player that have been
     * achieved with this weapon
     *
     * @return string The percentage of kills with this weapon
     */
    public String getKillPercentage() {
        return killPercentage;
    }

    /**
     * Returns the name of the weapon
     *
     * @return string The name of the weapon
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the number of shots the player has fired with this weapon
     *
     * @return int The number of shots with this weapon
     */
    public int getShots() {
        return this.shots;
    }
}
