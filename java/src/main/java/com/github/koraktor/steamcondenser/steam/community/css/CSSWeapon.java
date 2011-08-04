/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.css;

import org.w3c.dom.Element;

/**
 * Represents the stats for a Counter-Strike: Source weapon for a specific user
 *
 * @author Sebastian Staudt
 */
public class CSSWeapon {

    private boolean favorite;

    private int hits;

    private int kills;

    private String name;

    private int shots;

    /**
     * Creates a new instance of a Counter-Strike: Source weapon based on the
     * given XML data
     *
     * @param weaponName The name of the weapon
     * @param weaponsData The XML data of all weapons
     */
    public CSSWeapon(String weaponName, Element weaponsData) {
        this.name = weaponName;

        this.favorite = weaponsData.getElementsByTagName("favorite").item(0).getTextContent().equals(this.name);
        this.kills    = Integer.parseInt(weaponsData.getElementsByTagName(this.name + "_kills").item(0).getTextContent());

        if(!this.name.equals("grenade") && !this.name.equals("knife")) {
            this.hits  = Integer.parseInt(weaponsData.getElementsByTagName(this.name + "_hits").item(0).getTextContent());
            this.shots = Integer.parseInt(weaponsData.getElementsByTagName(this.name + "_shots").item(0).getTextContent());
        }
    }

    /**
     * Returns whether this weapon is the favorite weapon of this player
     *
     * @return <code>true</code> if this is the favorite weapon
     */
    public boolean isFavorite() {
        return this.favorite;
    }

    /**
     * Returns the accuracy of this player with this weapon
     *
     * @return The accuracy with this weapon
     */
    public float getAccuracy() {
        return (this.shots > 0) ? this.hits / this.shots : 0;
    }

    /**
     * Returns the number of hits achieved with this weapon
     *
     * @return The number of hits achieved
     */
    public int getHits() {
        return this.hits;
    }

    /**
     * Returns the number of kills achieved with this weapon
     *
     * @return The number of kills achieved
     */
    public int getKills() {
        return this.kills;
    }

    /**
     * Returns the kill-shot-ratio of this player with this weapon
     *
     * @return The kill-shot-ratio
     */
    public float getKsRatio() {
        return (this.shots > 0) ? this.kills / this.shots : 0;
    }

    /**
     * Returns the name of this weapon
     *
     * @return The name of this weapon
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the number of shots fired with this weapon
     *
     * @return The number of shots fired
     */
    public int getShots() {
        return this.shots;
    }

}
