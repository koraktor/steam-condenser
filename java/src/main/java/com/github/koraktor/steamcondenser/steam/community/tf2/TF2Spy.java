/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.tf2;

import org.w3c.dom.Element;

/**
 * Represents the stats for the Team Fortress 2 Spy class for a specific user
 *
 * @author Sebastian Staudt
 */
public class TF2Spy extends TF2Class {

    private int maxBackstabs;

    private int maxHealthLeeched;

    /**
     * Creates a new instance of the Spy class based on the given XML data
     *
     * @param classData The XML data for this Spy
     */
    public TF2Spy(Element classData) {
        super(classData);

        this.maxBackstabs      = Integer.parseInt(classData.getElementsByTagName("ibackstabs").item(0).getTextContent());
        this.maxHealthLeeched  = Integer.parseInt(classData.getElementsByTagName("ihealthpointsleached").item(0).getTextContent());
    }

    /**
     * Returns the maximum health leeched from enemies by the player in a single
     * life as a Spy
     *
     * @return Maximum health leeched
     */
    public int getMaxBackstabs() {
        return this.maxBackstabs;
    }

    /**
     * Returns the maximum health leeched from enemies by the player in a single
     * life as a Spy
     *
     * @return Maximum health leeched
     */
    public int getMaxHealthLeeched() {
        return this.maxHealthLeeched;
    }
}
