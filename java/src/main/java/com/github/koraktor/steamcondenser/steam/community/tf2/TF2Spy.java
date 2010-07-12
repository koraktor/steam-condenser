/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.tf2;

import org.w3c.dom.Element;

/**
 * Represents the stats for the Team Fortress 2 spy class for a specific user
 * @author  Sebastian Staudt
 */
public class TF2Spy extends TF2Class
{
    private int maxBackstabs;

    private int maxHealthLeeched;

    /**
     * Creates a new instance of TF2Spy based on the assigned XML data
     * @param classData
     */
    public TF2Spy(Element classData)
    {
	super(classData);

	this.maxBackstabs      = Integer.parseInt(classData.getElementsByTagName("ibackstabs").item(0).getTextContent());
	this.maxHealthLeeched  = Integer.parseInt(classData.getElementsByTagName("ihealthpointsleached").item(0).getTextContent());
    }

    public int getMaxBackstabs()
    {
	return this.maxBackstabs;
    }

    public int getMaxHealthLeeched()
    {
	return this.maxHealthLeeched;
    }
}
