/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser.steam.community;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The SteamId class represents a Steam Community profile (also called Steam ID)
 * @author Sebastian Staudt
 * @version $Id$
 */
public class SteamId
{
	private String customUrl;
	
	private String favoriteGame;
	
	private float favoriteGameHoursPlayed;
	
	private SteamId[] friends;
	
	private SteamGroup[] groups;
	
	private String headLine;
	
	private float hoursPlayed;
	
	private String id;
	
	private String imageUrl;
	
	private Map<String, String> links;
	
	private String location;
	
	private Date memberSince;
	
	private Map<String, Float> mostPlayedGames;
	
	private boolean isOnline;
	
	private String privacyState;
	
	private String realName;
	
	private String stateMessage;
	
	private String steamId;
	
	private long steamId64;
	
	private float steamRating;
	
	private String steamRatingText;
	
	private String summary;
	
	private boolean vacBanned;
	
	private int visibilityState;
	
	/**
	 * Creates a new SteamId object for the given ID and fetches the data
	 * @param id The numeric ID of the SteamID
	 * @throws IOException
	 * @throws DOMException
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public SteamId(int id)
		throws IOException, DOMException, ParseException, ParserConfigurationException, SAXException
	{
		this(Integer.toString(id), true);
	}
	
	/**
	 * Creates a new SteamId object for the given ID and fetches the data if
	 * fetchData is set to true
	 * @param id The numeric ID of the SteamID
	 * @param fetchData If set to true, the data of this SteamID will be fetched
	 *                  from Steam Community
	 * @throws IOException
	 * @throws DOMException
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public SteamId(int id, boolean fetchData)
		throws IOException, DOMException, ParseException, ParserConfigurationException, SAXException
	{
		this(Integer.toString(id), fetchData);
	}
	
	/**
	 * Creates a new SteamId object for the given ID and fetches the data
	 * @param id The ID, either numeric or the custom URL given by the person
	 *           to the SteamID
	 * @throws IOException
	 * @throws DOMException
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public SteamId(String id)
		throws IOException, DOMException, ParseException, ParserConfigurationException, SAXException
	{
		this(id, true);
	}
	
	/**
	 * Creates a new SteamId object for the given ID and fetches the data if
	 * fetchData is set to true
	 * @param id The ID, either numeric or the custom URL given by the person
	 *           to the SteamID
	 * @param fetchData If set to true, the data of this SteamID will be fetched
	 *                  from Steam Community
	 * @throws IOException
	 * @throws DOMException
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public SteamId(String id, boolean fetchData)
		throws IOException, DOMException, ParseException, ParserConfigurationException, SAXException
	{
		this.id = id;
		
		if(fetchData)
		{
			this.fetchData();
		}
	}
	
	/**
	 * This method fetches the data of this person's SteamID
	 * @throws IOException
	 * @throws DOMException
	 * @throws ParserConfigurationException
	 * @throws ParseException
	 * @throws SAXException
	 */
	private void fetchData()
		throws IOException, DOMException, ParserConfigurationException, ParseException, SAXException
	{
		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Element profile = parser.parse("http://www.steamcommunity.com/id/" + this.id + "?xml=1").getDocumentElement();

		String avatarIconUrl = profile.getElementsByTagName("avatarIcon").item(0).getTextContent();
		this.imageUrl = avatarIconUrl.substring(0, avatarIconUrl.length() - 4);
		this.isOnline = (profile.getElementsByTagName("onlineState").item(0).getTextContent() == "online");
		this.privacyState = profile.getElementsByTagName("privacyState").item(0).getTextContent();
		this.stateMessage = profile.getElementsByTagName("stateMessage").item(0).getTextContent();
		this.steamId = profile.getElementsByTagName("steamID").item(0).getTextContent();
		this.steamId64 = Long.parseLong(profile.getElementsByTagName("steamID64").item(0).getTextContent());
		this.vacBanned = (profile.getElementsByTagName("vacBanned").item(0).getTextContent() == "1");
		this.visibilityState = Integer.parseInt(profile.getElementsByTagName("visibilityState").item(0).getTextContent());

		if(this.privacyState.compareTo("public") == 0)
		{
			this.customUrl               = profile.getElementsByTagName("customURL").item(0).getTextContent();
			this.favoriteGame            = ((Element) profile.getElementsByTagName("favoriteGame").item(0)).getElementsByTagName("name").item(0).getTextContent();
			this.favoriteGameHoursPlayed = Float.parseFloat(((Element) profile.getElementsByTagName("favoriteGame").item(0)).getElementsByTagName("hoursPlayed2wk").item(0).getTextContent());
			this.headLine                = profile.getElementsByTagName("headline").item(0).getTextContent();
			this.hoursPlayed             = Float.parseFloat(profile.getElementsByTagName("hoursPlayed2Wk").item(0).getTextContent());
			this.location                = profile.getElementsByTagName("location").item(0).getTextContent();
			this.memberSince             = DateFormat.getDateInstance(DateFormat.LONG, Locale.ENGLISH).parse(profile.getElementsByTagName("memberSince").item(0).getTextContent());
			this.realName                = profile.getElementsByTagName("realname").item(0).getTextContent();
			String[] steamRating = profile.getElementsByTagName("steamRating").item(0).getTextContent().split(" - ");
			this.steamRating             = Float.parseFloat(steamRating[0]);
			this.steamRatingText         = steamRating[1];
			this.summary                 = profile.getElementsByTagName("summary").item(0).getTextContent();

			this.mostPlayedGames = new HashMap<String, Float>();
			NodeList mostPlayedGames = ((Element) profile.getElementsByTagName("mostPlayedGames").item(0)).getElementsByTagName("mostPlayedGame");
			for(int i = 0; i < mostPlayedGames.getLength(); i++)
			{
				Element mostPlayedGame = (Element) mostPlayedGames.item(i);
				this.mostPlayedGames.put(mostPlayedGame.getElementsByTagName("gameName").item(0).getTextContent(), Float.parseFloat(mostPlayedGame.getElementsByTagName("hoursPlayed").item(0).getTextContent()));
			}

			NodeList friends = ((Element) profile.getElementsByTagName("friends").item(0)).getElementsByTagName("friend");
			this.friends = new SteamId[friends.getLength()];
			for(int i = 0; i < friends.getLength(); i++)
			{
				Element friend = (Element) friends.item(i);
				this.friends[i] = new SteamId(friend.getElementsByTagName("steamID64").item(0).getTextContent(), false);
			}

			NodeList groups = ((Element) profile.getElementsByTagName("groups").item(0)).getElementsByTagName("group");
			this.groups = new SteamGroup[groups.getLength()];
			for(int i = 0; i < groups.getLength(); i++)
			{
				Element group = (Element) groups.item(i);
				this.groups[i] = new SteamGroup(group.getElementsByTagName("groupID64").item(0).getTextContent());
			}
		}
	}
	
	public String getAvatarFullUrl()
	{
		return this.imageUrl + "_full.jpg";
	}
	
	public String getAvatarIconUrl()
	{
		return this.imageUrl + ".jpg";
	}
	
	public String getAvatarMediumUrl()
	{
		return this.imageUrl + "_medium.jpg";
	}
	
	public String getCustomUrl()
	{
		return this.customUrl;
	}
	
	public String getFavoriteGame()
	{
		return this.favoriteGame;
	}
	
	public float getFavoriteGameHoursPlayed()
	{
		return this.favoriteGameHoursPlayed;
	}
	
	public SteamId[] getFriends()
	{
		return this.friends;
	}
	
	public SteamGroup[] getGroups()
	{
		return this.groups;
	}
	
	public String getHeadLine()
	{
		return this.headLine;
	}
	
	public float getHoursPlayed()
	{
		return this.hoursPlayed;
	}
	
	public String getId()
	{
		return this.id;
	}
	
	public Map<String, String> getLinks()
	{
		return this.links;
	}
	
	public String getLocation()
	{
		return this.location;
	}
	
	/**
	 * Returns the date of registration for the Steam account belonging to this
	 * SteamID
	 * @return The date of the Steam account registration
	 */
	public Date getMemberSince()
	{
		return this.memberSince;
	}
	
	/**
	 * Returns whether the owner of this SteamID is currently logged into Steam
	 * @return True if the user is currenly online or false otherwise
	 */
	public boolean isOnline()
	{
		return this.isOnline;
	}
	
	public String getRealName()
	{
		return this.realName;
	}
	
	public String getStateMessage()
	{
		return this.stateMessage;
	}
	
	public String getSteamId()
	{
		return this.steamId;
	}
	
	public long getSteamId64()
	{
		return this.steamId64;
	}
	
	public float getSteamRating()
	{
		return this.steamRating;
	}
	
	public String getSteamRatingText()
	{
		return this.steamRatingText;
	}
	
	public String getSummary()
	{
		return this.summary;
	}
	
	public boolean getVacBanned()
	{
		return this.vacBanned;
	}
	
	public int getVisibilityState()
	{
		return this.visibilityState;
	}
}
