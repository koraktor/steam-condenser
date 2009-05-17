/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
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

import steamcondenser.steam.community.tf2.TF2Stats;

/**
 * The GameStats class represents the game statistics for a single user and a
 * specific game
 * @author Sebastian Staudt
 */
public class GameStats {

	protected ArrayList<GameAchievement> achievements;
	protected int appId;
	protected String customUrl;
	protected String gameFriendlyName;
	protected String gameName;
	protected float hoursPlayed;
	protected String privacyState;
	protected long steamId64;
	protected Element xmlData;

	public static GameStats createGameStats(String customUrl, String gameName)
			throws ParserConfigurationException, SAXException, IOException {
		if(gameName.equals("TF2")) {
			return new TF2Stats(customUrl);
		} else {
			return new GameStats(customUrl, gameName);
		}
	}

	public static GameStats createGameStats(long steamId64, String gameName)
			throws ParserConfigurationException, SAXException, IOException {
		if(gameName.equals("TF2")) {
			return new TF2Stats(steamId64);
		} else {
			return new GameStats(steamId64, gameName);
		}
	}

	protected GameStats(String customUrl, String gameName)
			throws ParserConfigurationException, SAXException, IOException {
		this.customUrl = customUrl;
		this.gameName  = gameName;
		this.fetch();
	}

	protected GameStats(long steamId64, String gameName)
			throws ParserConfigurationException, SAXException, IOException {
		this.steamId64 = steamId64;
		this.gameName  = gameName;
		this.fetch();
	}

	protected void fetch()
			throws ParserConfigurationException, SAXException, IOException {
		String url;
		if(this.customUrl == null) {
			url = "http://www.steamcommunity.com/id/" + this.customUrl + "/stats/" + this.gameName + "?xml=1";
		} else {
			url = "http://www.steamcommunity.com/id/" + this.steamId64 + "/stats/" + this.gameName + "?xml=1";
		}

		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		this.xmlData = parser.parse(url).getDocumentElement();

		this.privacyState = this.xmlData.getElementsByTagName("privacyState").item(0).getTextContent();
		if(this.isPublic()) {
			this.appId            = Integer.parseInt(((Element) this.xmlData.getElementsByTagName("game").item(0)).getElementsByTagName("gameLink").item(0).getTextContent().replace("http://store.steampowered.com/app/", ""));
			this.gameFriendlyName = ((Element) this.xmlData.getElementsByTagName("game").item(0)).getElementsByTagName("gameFriendlyName").item(0).getTextContent();
			this.gameName         = ((Element) this.xmlData.getElementsByTagName("game").item(0)).getElementsByTagName("gameName").item(0).getTextContent();
			this.hoursPlayed      = Float.parseFloat(((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("hoursPlayed").item(0).getTextContent());
		}
	}

	/**
	 * Returns the achievements for this stats' user and game. If the
	 * achievements haven't been parsed already, parsing is done now.
	 * @return
	 */
	public ArrayList<GameAchievement> getAchievements() {
		if(this.achievements == null) {
			this.achievements = new ArrayList<GameAchievement>();

			NodeList achievementsNode = this.xmlData.getElementsByTagName("achievements").item(0).getChildNodes();
			for(int i = 0; i < achievementsNode.getLength(); i++) {
				Element achievement = (Element) achievementsNode.item(i);
				String achievementName = achievement.getElementsByTagName("name").item(0).getTextContent();
				boolean achievementDone = achievement.getElementsByTagName("closed").item(0).getTextContent().equals("1");
				if(this.customUrl == null) {
					this.achievements.add(new GameAchievement(this.appId, achievementName, achievementDone));
				} else {
					this.achievements.add(new GameAchievement(this.appId, achievementName, achievementDone));
				}
			}
		}

		return achievements;
	}

	public String getGameFriendlyName() {
		return this.gameFriendlyName;
	}

	public String getGameName() {
		return this.gameName;
	}

	public float getHoursPlayed() {
		return this.hoursPlayed;
	}

	protected boolean isPublic() {
		return this.privacyState.equals("public");
	}
}
