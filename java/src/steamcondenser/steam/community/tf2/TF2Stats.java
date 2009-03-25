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

    /**
     * Creates a new object holding Team Fortress 2 statistics for the given
     * user
     * @param steamId
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public TF2Stats(String customUrl)
            throws ParserConfigurationException, SAXException, IOException
    {
        super(customUrl, "TF2");
    }

    /**
     * Creates a new object holding Team Fortress 2 statistics for the given
     * user
     * @param steamId
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public TF2Stats(long steamId64)
            throws ParserConfigurationException, SAXException, IOException
    {
        super(steamId64, "TF2");
    }

    /**
     * Returns the statistics for all Team Fortress 2 classes for this user
     * @return An array storing individual TF2Class objects for each Team
     *         Fortress 2 class
     */
    public ArrayList<TF2Class> getClassStats()
    {
        if(this.classStats == null) {
            this.classStats = new ArrayList<TF2Class>();
            NodeList classes = ((Element)this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("classData");
            for(int i = 0; i < classes.getLength(); i ++) {
                this.classStats.add(TF2ClassFactory.getTF2Class((Element)classes.item(i)));
            }
        }

        return this.classStats;
    }
}
