/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.css;

import org.w3c.dom.Element;

/**
 * CSSWeapon holds statistical information about weapons used by a player in
 * Counter-Strike: Source.
 *
 * @author Sebastian Staudt
 */
public class CSSWeapon {

    private float accuracy;

    private boolean favorite;

    private int hits;

    private int kills;

    private float ksRatio;

    private String name;

    private int shots;

    /**
     * Creates a new instance of CSSWeapon based on the assigned XML data
     *
     * @param weaponName
     * @param weaponsData
     */
    public CSSWeapon(String weaponName, Element weaponsData) {
        this.name = weaponName;

        this.favorite = weaponsData.getElementsByTagName("favorite").item(0).getTextContent().equals(this.name);
        this.kills    = Integer.parseInt(weaponsData.getElementsByTagName(this.name + "_kills").item(0).getTextContent());

        if(!this.name.equals("grenade") && !this.name.equals("knife")) {
            this.hits  = Integer.parseInt(weaponsData.getElementsByTagName(this.name + "_hits").item(0).getTextContent());
            this.shots = Integer.parseInt(weaponsData.getElementsByTagName(this.name + "_shots").item(0).getTextContent());

            this.accuracy = this.hits / this.shots;
            this.ksRatio  = this.kills / this.shots;
        }
    }

    /**
     * Returns whether this weapon is the favorite weapon of this player
     *
     * @return true if this is the favorite weapon
     */
    public boolean isFavorite() {
        return this.favorite;
    }

    public float getAccuracy() {
        return this.accuracy;
    }

    public int getHits() {
        return this.hits;
    }

    public int getKills() {
        return this.kills;
    }

    public float getKsRatio() {
        return this.ksRatio;
    }

    public String getName() {
        return this.name;
    }

    public int getShots() {
        return this.shots;
    }

}
