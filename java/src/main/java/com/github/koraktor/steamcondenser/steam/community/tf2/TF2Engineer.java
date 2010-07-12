/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.tf2;

import org.w3c.dom.Element;

/**
 * Represents the stats for the Team Fortress 2 engineer class for a specific
 * user
 * @author  Sebastian Staudt
 */
public class TF2Engineer extends TF2Class
{
    private int maxBuildingsBuilt;

    private int maxSentryKills;

    private int maxTeleports;

    /**
     * Creates a new instance of TF2Engineer based on the assigned XML data
     * @param classData
     */
    public TF2Engineer(Element classData)
    {
	super(classData);

	this.maxBuildingsBuilt = Integer.parseInt(classData.getElementsByTagName("ibuildingsbuilt").item(0).getTextContent());
	this.maxSentryKills    = Integer.parseInt(classData.getElementsByTagName("isentrykills").item(0).getTextContent());
	this.maxTeleports      = Integer.parseInt(classData.getElementsByTagName("inumteleports").item(0).getTextContent());
    }

    public int getMaxBuildingsBuilt()
    {
	return this.maxBuildingsBuilt;
    }

    public int getMaxSentryKills()
    {
	return this.maxSentryKills;
    }

    public int getMaxTeleports()
    {
	return this.maxTeleports;
    }

}
