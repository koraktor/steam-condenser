/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.alien_swarm;

import org.w3c.dom.Element;

import com.github.koraktor.steamcondenser.steam.community.GameWeapon;

/**
 * This class holds statistical information about weapons used by a player
 * in Alien Swarm
 *
 * @author Sebastian Staudt
 */
public class AlienSwarmWeapon extends GameWeapon {

    private float accuracy;

    private int damage;

    private int friendlyFire;

    private String name;

    /**
     * Creates a new weapon instance based on the assigned weapon XML data
     *
     * @param weaponData The data representing this weapon
     */
    public AlienSwarmWeapon(Element weaponData) {
        super(weaponData);

        this.accuracy     = Float.parseFloat(weaponData.getElementsByTagName("accuracy").item(0).getTextContent());
        this.damage       = Integer.parseInt(weaponData.getElementsByTagName("damage").item(0).getTextContent());
        this.friendlyFire = Integer.parseInt(weaponData.getElementsByTagName("friendlyfire").item(0).getTextContent());
        this.name         = weaponData.getElementsByTagName("name").item(0).getTextContent();
        this.shots        = Integer.parseInt(weaponData.getElementsByTagName("shotsfired").item(0).getTextContent());
    }

    /**
     * Returns the accuracy of the player with this weapon
     *
     * @return The accuracy of the player with this weapon
     */
    public float getAccuracy() {
        return this.accuracy;
    }

    /**
     * Returns the damage achieved with this weapon
     *
     * @return The damage achieved with this weapon
     */
    public int getDamage() {
        return this.damage;
    }

    /**
     * Returns the damage dealt to team mates with this weapon
     *
     * @return The damage dealt to team mates with this weapon
     */
    public int getFriendlyFire() {
        return this.friendlyFire;
    }

    /**
     * Returns the name of this weapon
     *
     * @return The name of this weapon
     */
    public String getName() {
        return this.name;
    }

}
