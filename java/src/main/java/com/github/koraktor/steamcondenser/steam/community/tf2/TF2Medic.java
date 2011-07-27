/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.tf2;

import org.w3c.dom.Element;

/**
 * Represents the stats for the Team Fortress 2 Medic class for a specific user
 *
 * @author Sebastian Staudt
 */
public class TF2Medic extends TF2Class
{
    private int maxHealthHealed;

    private int maxUbercharges;

    /**
     * Creates a new instance of the Medic class based on the given XML data
     *
     * @param classData The XML data for this Medic
     */
    public TF2Medic(Element classData)
    {
        super(classData);

        this.maxHealthHealed = Integer.parseInt(classData.getElementsByTagName("ihealthpointshealed").item(0).getTextContent());
        this.maxUbercharges  = Integer.parseInt(classData.getElementsByTagName("inuminvulnerable").item(0).getTextContent());
    }

    /**
     * Returns the maximum health healed for teammates by the player in a
     * single life as a Medic
     *
     * @return Maximum health healed
     */
    public int getMaxHealthHealed()
    {
        return this.maxHealthHealed;
    }

    /**
     * Returns the maximum number of ÜberCharges provided by the player in a
     * single life as a Medic
     *
     * @return Maximum number of ÜberCharges
     */
    public int getMaxUbercharges()
    {
        return this.maxUbercharges;
    }
}
