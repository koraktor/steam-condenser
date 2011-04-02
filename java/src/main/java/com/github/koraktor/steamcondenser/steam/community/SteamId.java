/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;

/**
 * The SteamId class represents a Steam Community profile (also called Steam ID)
 *
 * @author Sebastian Staudt
 */
public class SteamId {

	private static Map<Object, SteamId> steamIds = new HashMap<Object, SteamId>();

	private String customUrl;
	private String favoriteGame;
	private float favoriteGameHoursPlayed;
	private long fetchTime;
	private SteamId[] friends;
	private Map<String, String> games;
	private SteamGroup[] groups;
	private String headLine;
	private float hoursPlayed;
	private String imageUrl;
	private Map<String, String> links;
	private String location;
	private Date memberSince;
	private Map<String, Float> mostPlayedGames;
	private String onlineState;
	private String privacyState;
	private String realName;
	private String stateMessage;
	private String nickname;
	private long steamId64;
	private float steamRating;
	private String steamRatingText;
	private String summary;
	private boolean vacBanned;
	private int visibilityState;

    /**
     * Converts the 64bit SteamID as used and reported by the Steam Community
     * to a SteamID reported by game servers
     */
    public static String convertCommunityIdToSteamId(long communityId)
            throws SteamCondenserException {
        long steamId1 = communityId % 2;
        long steamId2 = communityId - 76561197960265728L;

        if(steamId2 <= 0) {
            throw new SteamCondenserException("SteamID " + communityId + " is too small.");
        }

        steamId2 = (steamId2 - steamId1) / 2;

        return "STEAM_0:" + steamId1 + ":" + steamId2;
    }

    /**
     * Converts the SteamID as reported by game servers to a 64bit SteamID
     */
    public static long convertSteamIdToCommunityId(String steamId)
            throws SteamCondenserException {
        if(steamId.equals("STEAM_ID_LAN") || steamId.equals("BOT")) {
            throw new SteamCondenserException("Cannot convert SteamID \"" + steamId + "\" to a community ID.");
        }
        if(!steamId.matches("^STEAM_[0-1]:[0-1]:[0-9]+$")) {
            throw new SteamCondenserException("SteamID \"" + steamId + "\" doesn't have the correct format.");
        }

        String[] tmpId = steamId.substring(6).split(":");

        return Long.valueOf(tmpId[1]) + Long.valueOf(tmpId[2]) * 2 + 76561197960265728L;
    }

	public static SteamId create(long id)
	 	throws SteamCondenserException {
		return SteamId.create((Object) id, true, false);
	}

	public static SteamId create(String id)
		throws SteamCondenserException {
		return SteamId.create((Object) id, true, false);
	}

	public static SteamId create(long id, boolean fetch)
		throws SteamCondenserException {
		return SteamId.create((Object) id, fetch, false);
	}

	public static SteamId create(String id, boolean fetch)
		throws SteamCondenserException {
		return SteamId.create((Object) id, fetch, false);
	}

	public static SteamId create(long id, boolean fetch, boolean bypassCache)
		throws SteamCondenserException {
		return SteamId.create((Object) id, fetch, bypassCache);
	}

	public static SteamId create(String id, boolean fetch, boolean bypassCache)
		throws SteamCondenserException {
		return SteamId.create((Object) id, fetch, bypassCache);
	}

	private static SteamId create(Object id, boolean fetch, boolean bypassCache)
		throws SteamCondenserException {
		if(SteamId.isCached(id) && !bypassCache) {
			SteamId steamId = SteamId.steamIds.get(id);
			if(fetch && !steamId.isFetched()) {
				steamId.fetchData();
			}
			return steamId;
		} else {
			return new SteamId(id, fetch);
		}
	}

	public static boolean isCached(Object id) {
		return SteamId.steamIds.containsKey(id);
	}

	/**
	 * Creates a new SteamId object for the given ID and fetches the data if
	 * fetchData is set to true
	 *
	 * @param id
	 *            Either the custom URL or numeric ID of the SteamID
	 * @param fetchData
	 *            If set to true, the data of this SteamID will be fetched from
	 *            Steam Community
	 * @throws SteamCondenserException
	 */
	private SteamId(Object id, boolean fetchData)
			throws SteamCondenserException {
		if(id instanceof String) {
			this.customUrl = (String) id;
		} else {
			this.steamId64 = (Long) id;
		}

		if(fetchData) {
			this.fetchData();
		}

		this.cache();
	}

	public boolean cache() {
		if(!SteamId.steamIds.containsKey(this.steamId64)) {
			SteamId.steamIds.put(this.steamId64, this);
			if(this.customUrl != null && !SteamId.steamIds.containsKey(this.customUrl)) {
				SteamId.steamIds.put(this.customUrl, this);
			}
			return true;
		}
		return false;
	}

	/**
	 * This method fetches the data of this person's SteamID
	 *
	 * @throws SteamCondenserException
	 */
	public void fetchData()
			throws SteamCondenserException {
		try {
			String url = this.getBaseUrl() + "?xml=1";
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Element profile = parser.parse(url).getDocumentElement();

			if(profile.getElementsByTagName("error").getLength() > 0) {
				throw new SteamCondenserException(profile.getElementsByTagName("error").item(0).getTextContent());
			}

            this.nickname  = profile.getElementsByTagName("steamID").item(0).getTextContent();
            this.steamId64 = Long.parseLong(profile.getElementsByTagName("steamID64").item(0).getTextContent());
            this.vacBanned = (profile.getElementsByTagName("vacBanned").item(0).getTextContent().equals("1"));

            if(profile.getElementsByTagName("privacyMessage").getLength() > 0) {
                throw new SteamCondenserException(profile.getElementsByTagName("privacyMessage").item(0).getTextContent());
            }

			String avatarIconUrl = profile.getElementsByTagName("avatarIcon").item(0).getTextContent();
			this.imageUrl = avatarIconUrl.substring(0, avatarIconUrl.length() - 4);
			this.onlineState = profile.getElementsByTagName("onlineState").item(0).getTextContent();
			this.privacyState = profile.getElementsByTagName("privacyState").item(0).getTextContent();
			this.stateMessage = profile.getElementsByTagName("stateMessage").item(0).getTextContent();
			this.visibilityState = Integer.parseInt(profile.getElementsByTagName("visibilityState").item(0).getTextContent());

			if(this.privacyState.compareTo("public") == 0) {
				this.customUrl = profile.getElementsByTagName("customURL").item(0).getTextContent();
                if(this.customUrl.isEmpty()) {
                    this.customUrl = null;
                }

				Element favoriteGame = (Element) profile.getElementsByTagName("favoriteGame").item(0);
				if(favoriteGame != null) {
					this.favoriteGame = favoriteGame.getElementsByTagName("name").item(0).getTextContent();
					this.favoriteGameHoursPlayed = Float.parseFloat(favoriteGame.getElementsByTagName("hoursPlayed2wk").item(0).getTextContent());
				}
				this.headLine = profile.getElementsByTagName("headline").item(0).getTextContent();
				this.hoursPlayed = Float.parseFloat(profile.getElementsByTagName("hoursPlayed2Wk").item(0).getTextContent());
				this.location = profile.getElementsByTagName("location").item(0).getTextContent();
				this.memberSince = DateFormat.getDateInstance(DateFormat.LONG,Locale.ENGLISH).parse(profile.getElementsByTagName("memberSince").item(0).getTextContent());
				this.realName = profile.getElementsByTagName("realname").item(0).getTextContent();
				this.steamRating = Float.parseFloat(profile.getElementsByTagName("steamRating").item(0).getTextContent());
				this.summary = profile.getElementsByTagName("summary").item(0).getTextContent();

				this.mostPlayedGames = new HashMap<String, Float>();
				Element mostPlayedGamesNode = (Element) profile.getElementsByTagName("mostPlayedGames").item(0);
				if(mostPlayedGamesNode != null) {
					NodeList mostPlayedGameList = mostPlayedGamesNode.getElementsByTagName("mostPlayedGame");
					for(int i = 0; i < mostPlayedGameList.getLength(); i++) {
						Element mostPlayedGame = (Element) mostPlayedGameList.item(i);
						this.mostPlayedGames.put(mostPlayedGame.getElementsByTagName("gameName").item(0).getTextContent(), Float.parseFloat(mostPlayedGame.getElementsByTagName("hoursPlayed").item(0).getTextContent()));
					}
				}

				Element groupsNode = (Element) profile.getElementsByTagName(
						"groups").item(0);
				if(groupsNode != null) {
					NodeList groupsNodeList = ((Element) groupsNode).getElementsByTagName("group");
					this.groups = new SteamGroup[groupsNodeList.getLength()];
					for(int i = 0; i < groupsNodeList.getLength(); i++) {
						Element group = (Element) groupsNodeList.item(i);
						this.groups[i] = SteamGroup.create(Long.parseLong(group.getElementsByTagName("groupID64").item(0).getTextContent()), false);
					}
				}

				this.links = new HashMap<String, String>();
				Element weblinksNode = (Element) profile.getElementsByTagName("weblinks").item(0);
				if(weblinksNode != null) {
					NodeList weblinksList = weblinksNode.getElementsByTagName("weblink");
					for(int i = 0; i < weblinksList.getLength(); i++) {
						Element weblink = (Element) weblinksList.item(i);
						this.links.put(weblink.getElementsByTagName("title").item(0).getTextContent(), weblink.getElementsByTagName("link").item(0).getTextContent());
					}
				}
			}
		} catch(Exception e) {
			throw new SteamCondenserException("XML data could not be parsed.");
		}

		this.fetchTime = new Date().getTime();
	}

	/**
	 * Fetches the friends of this user
	 *
	 * @throws SteamCondenserException
	 */
	private void fetchFriends()
			throws SteamCondenserException {
		try {
			String url = this.getBaseUrl() + "/friends?xml=1";
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Element friendsData = parser.parse(url).getDocumentElement();

			Element friendsNode = (Element) friendsData.getElementsByTagName("friends").item(0);
			NodeList friendsNodeList = ((Element) friendsNode).getElementsByTagName("friend");
			this.friends = new SteamId[friendsNodeList.getLength()];
			for(int i = 0; i < friendsNodeList.getLength(); i++) {
				Element friend = (Element) friendsNodeList.item(i);
				this.friends[i] = SteamId.create(Long.parseLong(friend.getTextContent()), false);
			}
		} catch(Exception e) {
			throw new SteamCondenserException("XML data could not be parsed.");
		}
	}

	/**
	 * Fetches the games this user owns
	 *
	 * @throws SteamCondenserException
	 */
	private void fetchGames()
			throws SteamCondenserException {
		try {
			String url = this.getBaseUrl() + "/games?xml=1";
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Element gamesData = parser.parse(url).getDocumentElement();

			Element gamesNode = (Element) gamesData.getElementsByTagName("games").item(0);
			NodeList gamesNodeList = ((Element) gamesNode).getElementsByTagName("game");
			this.games = new HashMap<String, String>();
			for(int i = 0; i < gamesNodeList.getLength(); i++) {
				Element game = (Element) gamesNodeList.item(i);
				String gameName = game.getElementsByTagName("name").item(0).getTextContent();
				Node globalStatsLinkNode = game.getElementsByTagName("globalStatsLink").item(0);
				if(globalStatsLinkNode != null) {
                    String friendlyName = globalStatsLinkNode.getTextContent();
					Pattern regex = Pattern.compile("http://steamcommunity.com/stats/([^?/]+)/achievements/");
					Matcher matcher = regex.matcher(friendlyName);
					matcher.find(0);
					friendlyName = matcher.group(1).toLowerCase();
                    this.games.put(gameName, friendlyName);
				} else {
					this.games.put(gameName, null);
				}
			}
		} catch(Exception e) {
			throw new SteamCondenserException("XML data could not be parsed.");
		}
	}

	public String getAvatarFullUrl() {
		return this.imageUrl + "_full.jpg";
	}

	public String getAvatarIconUrl() {
		return this.imageUrl + ".jpg";
	}

	public String getAvatarMediumUrl() {
		return this.imageUrl + "_medium.jpg";
	}

	public String getBaseUrl() {
		if(this.customUrl == null) {
			return "http://steamcommunity.com/profiles/" + this.steamId64;
		} else {
			return "http://steamcommunity.com/id/" + this.customUrl;
		}
	}

	public String getCustomUrl() {
		return this.customUrl;
	}

	public String getFavoriteGame() {
		return this.favoriteGame;
	}

	public float getFavoriteGameHoursPlayed() {
		return this.favoriteGameHoursPlayed;
	}

	public long getFetchTime() {
		return this.fetchTime;
	}

	public SteamId[] getFriends()
			throws SteamCondenserException {
		if(this.friends == null) {
			this.fetchFriends();
		}
		return this.friends;
	}

	public Map<String, String> getGames()
			throws SteamCondenserException {
		if(this.games == null) {
			this.fetchGames();
		}
		return this.games;
	}

	public GameStats getGameStats(String gameName)
			throws SteamCondenserException {
		String friendlyName;

		this.getGames();

		if(this.games.containsKey(gameName)) {
			friendlyName = this.games.get(gameName);
		} else if(this.games.containsValue(gameName.toLowerCase())) {
			friendlyName = gameName.toLowerCase();
		} else {
			throw new SteamCondenserException("Stats for game " + gameName + " do not exist.");
		}

		if(this.customUrl == null) {
			return GameStats.createGameStats(this.steamId64, friendlyName);
		} else {
			return GameStats.createGameStats(this.customUrl, friendlyName);
		}
	}

	public SteamGroup[] getGroups() {
		return this.groups;
	}

	public String getHeadLine() {
		return this.headLine;
	}

	public float getHoursPlayed() {
		return this.hoursPlayed;
	}

	public Map<String, String> getLinks() {
		return this.links;
	}

	public String getLocation() {
		return this.location;
	}

	/**
	 * Returns the date of registration for the Steam account belonging to this
	 * SteamID
	 *
	 * @return The date of the Steam account registration
	 */
	public Date getMemberSince() {
		return this.memberSince;
	}

	public String getNickname() {
		return this.nickname;
	}

	public String getRealName() {
		return this.realName;
	}

	public String getStateMessage() {
		return this.stateMessage;
	}

	public long getSteamId64() {
		return this.steamId64;
	}

	public float getSteamRating() {
		return this.steamRating;
	}

	public String getSteamRatingText() {
		return this.steamRatingText;
	}

	public String getSummary() {
		return this.summary;
	}

	public boolean getVacBanned() {
		return this.vacBanned;
	}

	public int getVisibilityState() {
		return this.visibilityState;
	}

	/**
	 * Returns whether the owner of this SteamID is VAC banned
	 */
	public boolean isBanned() {
		return this.vacBanned;
	}

	public boolean isFetched() {
		return this.fetchTime != 0;
	}

	/**
	 * Returns whether the owner of this SteamId is playing a game
	 */
	public boolean isInGame() {
		return this.onlineState.equals("in-game");
	}

	/**
	 * Returns whether the owner of this SteamID is currently logged into Steam
	 *
	 * @return True if the user is currenly online or false otherwise
	 */
	public boolean isOnline() {
		return (this.onlineState.equals("online") || this.onlineState.equals("in-game"));
	}
}
