/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community.tf2;

import org.w3c.dom.Element;

/**
 * Represents the stats for the Team Fortress 2 sniper class for a specific user
 * @author  Sebastian Staudt
 */
public class TF2Sniper extends TF2Class
{
    private int maxHeadshots;

    /**
     * Creates a new instance of TF2Sniper based on the assigned XML data
     * @param classData
     */
    public TF2Sniper(Element classData)
    {
	super(classData);

	this.maxHeadshots = Integer.parseInt(classData.getElementsByTagName("iheadshots").item(0).getTextContent());
    }

    public int getMaxHeadshots()
    {
	return this.maxHeadshots;
    }
}
