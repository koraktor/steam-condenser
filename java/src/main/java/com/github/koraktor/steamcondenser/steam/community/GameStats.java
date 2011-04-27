/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;
import com.github.koraktor.steamcondenser.steam.community.css.CSSStats;
import com.github.koraktor.steamcondenser.steam.community.defense_grid.DefenseGridStats;
import com.github.koraktor.steamcondenser.steam.community.dods.DoDSStats;
import com.github.koraktor.steamcondenser.steam.community.l4d.L4D2Stats;
import com.github.koraktor.steamcondenser.steam.community.l4d.L4DStats;
import com.github.koraktor.steamcondenser.steam.community.portal2.Portal2Stats;
import com.github.koraktor.steamcondenser.steam.community.tf2.TF2Stats;

/**
 * The GameStats class represents the game statistics for a single user and a
 * specific game
 *
 * @author Sebastian Staudt
 */
public class GameStats {

    protected ArrayList<GameAchievement> achievements;

    protected int achievementsDone;

    protected int appId;

    protected String customUrl;

    protected String gameFriendlyName;

    protected String gameName;

    protected String hoursPlayed;

    protected String privacyState;

    protected Long steamId64;

    protected Element xmlData;

    public static GameStats createGameStats(Object steamId, String gameName)
			throws SteamCondenserException {
        if(gameName.equals("cs:s")) {
            return new CSSStats(steamId);
        } else if(gameName.equals("defensegrid:awakening")) {
			return new DefenseGridStats(steamId);
		} else if(gameName.equals("dod:s")) {
			return new DoDSStats(steamId);
		} else if(gameName.equals("l4d")) {
			return new L4DStats(steamId);
        } else if(gameName.equals("l4d2")) {
            return new L4D2Stats(steamId);
        } else if(gameName.equals("portal2")) {
            return new Portal2Stats(steamId);
		} else if(gameName.equals("tf2")) {
			return new TF2Stats(steamId);
		} else {
			return new GameStats(steamId, gameName);
		}
	}

	protected GameStats(Object steamId, String gameName)
			throws SteamCondenserException {
		if(steamId instanceof String) {
			this.customUrl = (String) steamId;
		} else if(steamId instanceof Long) {
			this.steamId64 = (Long) steamId;
		}
		this.gameName = gameName;
		this.fetch();
	}

	protected void fetch()
			throws SteamCondenserException {
		try {
            String url = this.getBaseUrl() + "?xml=all";

			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			this.xmlData = parser.parse(url).getDocumentElement();

            NodeList errorNode = this.xmlData.getElementsByTagName("error");
            if(errorNode.getLength() > 0) {
                throw new SteamCondenserException(errorNode.item(0).getTextContent());
            }

			this.privacyState = this.xmlData.getElementsByTagName("privacyState").item(0).getTextContent();
			if(this.isPublic()) {
				this.appId = Integer.parseInt(((Element) this.xmlData.getElementsByTagName("game").item(0)).getElementsByTagName("gameLink").item(0).getTextContent().replace("http://store.steampowered.com/app/", ""));
				this.gameFriendlyName = ((Element) this.xmlData.getElementsByTagName("game").item(0)).getElementsByTagName("gameFriendlyName").item(0).getTextContent();
				this.gameName = ((Element) this.xmlData.getElementsByTagName("game").item(0)).getElementsByTagName("gameName").item(0).getTextContent();

                Node hoursPlayedNode = ((Element) this.xmlData.getElementsByTagName("stats").item(0)).getElementsByTagName("hoursPlayed").item(0);
                if(hoursPlayedNode != null) {
	                this.hoursPlayed = hoursPlayedNode.getTextContent();
                }

                if(this.customUrl == null) {
                    this.customUrl = ((Element) this.xmlData.getElementsByTagName("player").item(0)).getElementsByTagName("customURL").item(0).getTextContent();
                }
                if(this.steamId64 == null) {
                    this.steamId64 = Long.parseLong(((Element) this.xmlData.getElementsByTagName("player").item(0)).getElementsByTagName("steamID64").item(0).getTextContent().trim());
                }
			}
		} catch(Exception e) {
			throw new SteamCondenserException("XML data could not be parsed.");
		}
	}

	/**
	 * @return Returns the achievements for this stats' user and game. If the
	 * achievements haven't been parsed already, parsing is done now.
	 */
	public ArrayList<GameAchievement> getAchievements() {
		if(this.achievements == null) {
			this.achievements = new ArrayList<GameAchievement>();
            this.achievementsDone = 0;

			NodeList achievementsList = ((Element) this.xmlData.getElementsByTagName("achievements").item(0)).getElementsByTagName("achievement");
			for(int i = 0; i < achievementsList.getLength(); i++) {
                Element achievementData = (Element) achievementsList.item(i);
                GameAchievement achievement = new GameAchievement(this.steamId64, this.appId, achievementData);
                if(achievement.isUnlocked()) {
                    this.achievementsDone += 1;
                }
                this.achievements.add(achievement);
			}
		}

		return achievements;
	}

    /**
     * Returns the count of achievements done by this player. If achievements
     * haven't been parsed yet, parsing is done now.
     *
     * @return The number of unlocked achievements
     */
    public int getAchievementsDone() {
        if(this.achievements == null) {
            this.getAchievements();
        }

        return this.achievementsDone;
    }

    /**
     * Returns a float value representing the percentage of achievements done by
     * this player. If achievements haven't been parsed yet, parsing is done
     * now.
     *
     * @return The percentage of unlocked achievements
     */
    public float getAchievementsPercentage() {
        return (float) this.getAchievementsDone() / this.achievements.size();
    }

    public String getBaseUrl() {
        if(this.customUrl == null) {
            return "http://steamcommunity.com/profiles/" + this.steamId64 + "/stats/" + this.gameName;
        } else {
            return "http://steamcommunity.com/id/" + this.customUrl + "/stats/" + this.gameName;
        }
    }

	public String getGameFriendlyName() {
		return this.gameFriendlyName;
	}

	public String getGameName() {
		return this.gameName;
	}

	public String getHoursPlayed() {
		return this.hoursPlayed;
	}

	protected boolean isPublic() {
		return this.privacyState.equals("public");
	}
}
