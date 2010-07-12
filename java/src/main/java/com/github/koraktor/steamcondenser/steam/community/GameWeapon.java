/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community;

import org.w3c.dom.Element;

/**
 * Super class for classes representing game weapons
 * @author Sebastian Staudt
 */
public abstract class GameWeapon {

    private float avgShotsPerKill;

    protected int kills;

    protected String id;

    protected int shots;

    public GameWeapon(Element weaponData) {
        this.kills = Integer.parseInt(weaponData.getElementsByTagName("kills").
        		item(0).getTextContent());
    }

    /**
     * Returns the average number of shots needed for a kill with this weapon.
     * Calculates the value if needed.
     * @return float
     */
    public float getAvgShotsPerKill() {
        if(this.avgShotsPerKill == 0) {
            this.avgShotsPerKill = this.shots / this.kills;
        }

        return this.avgShotsPerKill;
    }

    public String getId() {
        return this.id;
    }

    public int getKills() {
        return this.kills;
    }

    public int getShots() {
        return this.shots;
    }

}
