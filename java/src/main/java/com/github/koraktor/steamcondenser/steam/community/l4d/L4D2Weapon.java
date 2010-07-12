/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.l4d;

import org.w3c.dom.Element;

public class L4D2Weapon extends AbtractL4DWeapon {

    private int damage;

    private String weaponGroup;

    /**
     * Creates a new instance of L4D2Weapon based on the assigned XML data
     *
     * @param weaponData
     */
    public L4D2Weapon(Element weaponData) {
        super(weaponData);

        this.damage = Integer.valueOf(weaponData.getElementsByTagName("damage")
                .item(0).getTextContent());
        this.killPercentage = weaponData.getElementsByTagName("pctkills")
                .item(0).getTextContent();
        this.weaponGroup = weaponData.getAttribute("group");
    }

    public int getDamage() {
        return this.damage;
    }

    public String getWeaponGroup() {
        return this.weaponGroup;
    }
}
