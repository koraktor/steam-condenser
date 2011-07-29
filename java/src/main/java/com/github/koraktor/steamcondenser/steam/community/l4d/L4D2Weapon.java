/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.l4d;

import org.w3c.dom.Element;

/**
 * This class represents the statistics of a single weapon for a user in
 * Left4Dead 2
 *
 * @author Sebastian Staudt
 */
public class L4D2Weapon extends AbtractL4DWeapon {

    private int damage;

    private String weaponGroup;

    /**
     * Creates a new instance of a weapon based on the given XML data
     *
     * @param weaponData The XML data of this weapon
     */
    public L4D2Weapon(Element weaponData) {
        super(weaponData);

        this.damage = Integer.valueOf(weaponData.getElementsByTagName("damage")
                .item(0).getTextContent());
        this.killPercentage = Float.parseFloat(weaponData
                .getElementsByTagName("pctkills").item(0).getTextContent()
                .replace("%", "")) * 0.01f;
        this.weaponGroup = weaponData.getAttribute("group");
    }

    /**
     * Returns the amount of damage done by the player with this weapon
     *
     * @return The damage done by this weapon
     */
    public int getDamage() {
        return this.damage;
    }

    /**
     * Returns the weapon group this weapon belongs to
     *
     * @return The group this weapon belongs to
     */
    public String getWeaponGroup() {
        return this.weaponGroup;
    }
}
