/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.community.tf2;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import steamcondenser.steam.community.GameStats;

/**
 * The TF2Stats class represents the game statistics for a single user in Team
 * Fortress 2
 * 
 * @author Sebastian Staudt
 * @version $Id$
 */
public class TF2Stats extends GameStats
{
    private ArrayList<TF2Class> classStats;

    public TF2Stats(int steamId)
    throws ParserConfigurationException, SAXException, IOException
    {
	super(steamId, "TF2");
    }

    public ArrayList<TF2Class> getClassStats()
    {
	if(this.classStats == null)
	{
	    NodeList classes = ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("classData");
	    for(int i = 0; i < classes.getLength(); i ++)
	    {
		this.classStats.add(TF2ClassFactory.getTF2Class((Element) classes.item(i))); 
	    }
	}

	return this.classStats;
    }
}
