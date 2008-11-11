/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.community;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The GameStats class represents the game statistics for a single user and a
 * specific game
 * @author Sebastian Staudt
 * @version $Id$
 */
public class GameStats
{
    protected int accumulatedPoints;

    protected ArrayList<GameAchievement> achievements;

    protected int appId;

    protected String gameFriendlyName;

    protected String gameName;

    protected float hoursPlayed;

    protected String privacyState;

    protected String steamId;

    protected Element xmlData;

    public GameStats(String steamId, String gameName)
    throws ParserConfigurationException, SAXException, IOException
    {
	this.steamId = steamId;

	DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	this.xmlData = parser.parse("http://www.steamcommunity.com/id/" + this.steamId + "/stats/" + gameName + "?xml=1").getDocumentElement();

	this.privacyState = this.xmlData.getElementsByTagName("privacyState").item(0).getTextContent();
	if(this.privacyState == "public")
	{
	    this.accumulatedPoints = Integer.parseInt(((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("accumulatedPoints").item(0).getTextContent());
	    this.appId             = Integer.parseInt(((Element) this.xmlData.getElementsByTagName("game").item(0)).getElementsByTagName("gameLink").item(0).getTextContent().replace("http://store.steampowered.com/app/", ""));
	    this.gameFriendlyName  = ((Element) this.xmlData.getElementsByTagName("game").item(0)).getElementsByTagName("gameFriendlyName").item(0).getTextContent();
	    this.gameName          = ((Element) this.xmlData.getElementsByTagName("game").item(0)).getElementsByTagName("gameName").item(0).getTextContent();
	    this.hoursPlayed       = Float.parseFloat(((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("hoursPlayed").item(0).getTextContent());
	}
    }

    public int getAccumulatedPoints()
    {
	return this.accumulatedPoints;
    }

    public ArrayList<GameAchievement> getAchievements()
    {
	if(this.achievements == null)
	{
	    this.achievements = new ArrayList<GameAchievement>();

	    NodeList achievements = this.xmlData.getElementsByTagName("achievements").item(0).getChildNodes();
	    for(int i = 0; i < achievements.getLength(); i ++)
	    {
		Element achievement = (Element) achievements.item(i);
		this.achievements.add(new GameAchievement(this.steamId, this.appId, achievement.getElementsByTagName("name").item(0).getTextContent(), (achievement.getElementsByTagName("closed").item(0).getTextContent() == "1")));
	    }
	}

	return achievements;
    }

    public String getGameFriendlyName()
    {
	return this.gameFriendlyName;
    }

    public String getGameName()
    {
	return this.gameName;
    }

    public float getHoursPlayed()
    {
	return this.hoursPlayed;
    }
}
