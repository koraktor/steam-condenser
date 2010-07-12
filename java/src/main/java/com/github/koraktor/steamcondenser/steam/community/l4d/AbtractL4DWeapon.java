/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.l4d;

import org.w3c.dom.Element;
import com.github.koraktor.steamcondenser.steam.community.GameWeapon;

/**
 * @author Sebastian Staudt
 */
public abstract class AbtractL4DWeapon extends GameWeapon {

    protected String accuracy;

    protected String headshotPercentage;

    protected String killPercentage;

    protected String name;

    protected int shots;

    /**
     * Abstract base constructor which parses common data for both, L4DWeapon
     * and L4D2Weapon
     *
     * @param weaponData
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

    public String getAccuracy() {
        return this.accuracy;
    }

    public String getHeadshotPercentage() {
        return this.headshotPercentage;
    }

    public String getKillPercentage() {
        return killPercentage;
    }

    public String getName() {
        return this.name;
    }

    public int getShots() {
        return this.shots;
    }
}
