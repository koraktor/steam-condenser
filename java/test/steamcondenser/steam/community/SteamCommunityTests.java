/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.community;

import java.io.IOException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import steamcondenser.steam.community.tf2.TF2Stats;

/**
 * @author  Sebastian Staudt
 * @version $Id$
 */
public class SteamCommunityTests
{
    /**
     * This test tries to aquire information from a online Steam ID. This test
     * only passes if the parsing of the XML document works
     * @throws DOMException
     * @throws IOException
     * @throws ParseException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    @Test
    public void steamIdByCustomUrl()
    	throws DOMException, IOException, ParseException,
    	       ParserConfigurationException, SAXException
    {
	SteamId steamId = new SteamId("Koraktor");
	System.out.println(steamId.getSteamId64());
	TF2Stats tf2Stats = (TF2Stats) steamId.getGameStats("TF2");
	System.out.println(tf2Stats.getClassStats().get(0).getName());
    }

        /**
     * This test tries to aquire information from a online Steam ID. This test
     * only passes if the parsing of the XML document works
     * @throws DOMException
     * @throws IOException
     * @throws ParseException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    @Test
    public void steamIdBySteamId64()
    	throws DOMException, IOException, ParseException,
    	       ParserConfigurationException, SAXException
    {
	SteamId steamId = new SteamId(76561197961384956L);
	System.out.println(steamId.getSteamId64());
	TF2Stats tf2Stats = (TF2Stats) steamId.getGameStats("TF2");
	System.out.println(tf2Stats.getClassStats().get(0).getName());
    }
}
