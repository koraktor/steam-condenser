/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community;

import java.util.ArrayList;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringEscapeUtils;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;

/**
 * The SteamId class represents a Steam Community profile (also called Steam
 * ID)
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
    private HashMap<Integer, SteamGame> games;
    private SteamGroup[] groups;
    private String headLine;
    private float hoursPlayed;
    private String imageUrl;
    private Map<String, String> links;
    private String location;
    private Date memberSince;
    private Map<String, Float> mostPlayedGames;
    private String onlineState;
    private Map<Integer, int[]> playtimes;
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
     * Converts a 64bit numeric SteamID as used by the Steam Community to a
     * SteamID as reported by game servers
     *
     * @param communityId The SteamID string as used by the Steam Community
     * @return The converted SteamID, like <code>STEAM_0:0:12345</code>
     * @throws SteamCondenserException if the community ID is to small
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
     * Converts a SteamID as reported by game servers to a 64bit numeric
     * SteamID as used by the Steam Community
     *
     * @param steamId The SteamID string as used on servers, like
     *        <code>STEAM_0:0:12345</code>
     * @return The converted 64bit numeric SteamID
     * @throws SteamCondenserException if the SteamID doesn't have the correct
     *         format
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

    /**
     * Creates a new <code>SteamID</code> instance or gets an existing one
     * from the cache for the profile with the given ID
     *
     * @param id The 64bit SteamID of the player
     * @return The <code>SteamId</code> instance of the requested profile
     */
    public static SteamId create(long id)
            throws SteamCondenserException {
        return SteamId.create((Object) id, true, false);
    }

    /**
     * Creates a new <code>SteamID</code> instance or gets an existing one
     * from the cache for the profile with the given ID
     *
     * @param id The 64bit SteamID of the player
     * @return The <code>SteamId</code> instance of the requested profile
     */
    public static SteamId create(String id)
            throws SteamCondenserException {
        return SteamId.create((Object) id, true, false);
    }

    /**
     * Creates a new <code>SteamID</code> instance or gets an existing one
     * from the cache for the profile with the given ID
     *
     * @param id The 64bit SteamID of the player
     * @param fetch if <code>true</code> the profile's data is loaded into the
     *        object
     * @return The <code>SteamId</code> instance of the requested profile
     */
    public static SteamId create(long id, boolean fetch)
            throws SteamCondenserException {
        return SteamId.create((Object) id, fetch, false);
    }

    /**
     * Creates a new <code>SteamID</code> instance or gets an existing one
     * from the cache for the profile with the given ID
     *
     * @param id The 64bit SteamID of the player
     * @param fetch if <code>true</code> the profile's data is loaded into the
     *        object
     * @return The <code>SteamId</code> instance of the requested profile
     */
    public static SteamId create(String id, boolean fetch)
            throws SteamCondenserException {
        return SteamId.create((Object) id, fetch, false);
    }

    /**
     * Creates a new <code>SteamID</code> instance or gets an existing one
     * from the cache for the profile with the given ID
     *
     * @param id The 64bit SteamID of the player
     * @param fetch if <code>true</code> the profile's data is loaded into the
     *        object
     * @param bypassCache If <code>true</code> an already cached instance for
     *        this Steam ID will be ignored and a new one will be created
     * @return The <code>SteamId</code> instance of the requested profile
     */
    public static SteamId create(long id, boolean fetch, boolean bypassCache)
            throws SteamCondenserException {
        return SteamId.create((Object) id, fetch, bypassCache);
    }

    /**
     * Creates a new <code>SteamID</code> instance or gets an existing one
     * from the cache for the profile with the given ID
     *
     * @param id The custom URL of the Steam ID specified by player
     * @param fetch if <code>true</code> the profile's data is loaded into the
     *        object
     * @param bypassCache If <code>true</code> an already cached instance for
     *        this Steam ID will be ignored and a new one will be created
     * @return The <code>SteamId</code> instance of the requested profile
     */
    public static SteamId create(String id, boolean fetch, boolean bypassCache)
            throws SteamCondenserException {
        return SteamId.create((Object) id, fetch, bypassCache);
    }

    /**
     * Creates a new <code>SteamID</code> instance or gets an existing one
     * from the cache for the profile with the given ID
     *
     * @param id The custom URL of the Steam ID specified by player or the 64bit
     *        SteamID
     * @param fetch if <code>true</code> the profile's data is loaded into the
     *        object
     * @param bypassCache If <code>true</code> an already cached instance for
     *        this Steam ID will be ignored and a new one will be created
     * @return The <code>SteamId</code> instance of the requested profile
     */
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

    /**
     * Returns whether the requested Steam ID is already cached
     *
     * @param id The custom URL of the Steam ID specified by the player or the
     *        64bit SteamID
     * @return <code>true</code> if this Steam ID is already cached
     */
    public static boolean isCached(Object id) {
        return SteamId.steamIds.containsKey(id);
    }

    /**
     * Creates a new <code>SteamId</code> instance for the given ID
     *
     * @param id The custom URL of the group specified by the player or the
     *        64bit SteamID
     * @param fetchData if <code>true</code> the profile's data is loaded into
     *        the object
     * @throws SteamCondenserException if the Steam ID data is not available,
     *         e.g. when it is private
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

    /**
     * Saves this <code>SteamId</code> instance in the cache
     *
     * @return <code>false</code> if this group is already cached
     */
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
     * Fetchs data from the Steam Community by querying the XML version of the
     * profile specified by the ID of this Steam ID
     *
     * @throws SteamCondenserException if the Steam ID data is not available,
     *         e.g. when it is private, or when it cannot be parsed
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

            this.nickname  = StringEscapeUtils.unescapeXml(profile.getElementsByTagName("steamID").item(0).getTextContent());
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
                this.headLine = StringEscapeUtils.unescapeXml(profile.getElementsByTagName("headline").item(0).getTextContent());
                this.hoursPlayed = Float.parseFloat(profile.getElementsByTagName("hoursPlayed2Wk").item(0).getTextContent());
                this.location = profile.getElementsByTagName("location").item(0).getTextContent();
                this.memberSince = DateFormat.getDateInstance(DateFormat.LONG,Locale.ENGLISH).parse(profile.getElementsByTagName("memberSince").item(0).getTextContent());
                this.realName = StringEscapeUtils.unescapeXml(profile.getElementsByTagName("realname").item(0).getTextContent());
                this.steamRating = Float.parseFloat(profile.getElementsByTagName("steamRating").item(0).getTextContent());
                this.summary = StringEscapeUtils.unescapeXml(profile.getElementsByTagName("summary").item(0).getTextContent());

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
                        this.links.put(StringEscapeUtils.unescapeXml(weblink.getElementsByTagName("title").item(0).getTextContent()), weblink.getElementsByTagName("link").item(0).getTextContent());
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
     * <p>
     * This creates a new <code>SteamId</code> instance for each of the friends
     * without fetching their data.
     *
     * @see #getFriends
     * @see SteamId()
     * @throws SteamCondenserException if an error occurs while parsing the
     *         data
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
     * @see #getGames
     * @throws SteamCondenserException if an error occurs while parsing the
     *         data
     */
    private void fetchGames()
            throws SteamCondenserException {
        try {
            String url = this.getBaseUrl() + "/games?xml=1";
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Element gamesData = parser.parse(url).getDocumentElement();

            Element gamesNode = (Element) gamesData.getElementsByTagName("games").item(0);
            NodeList gamesNodeList = ((Element) gamesNode).getElementsByTagName("game");
            this.games = new HashMap<Integer, SteamGame>();
            this.playtimes = new HashMap<Integer, int[]>();
            for(int i = 0; i < gamesNodeList.getLength(); i++) {
                Element gameData = (Element) gamesNodeList.item(i);
                SteamGame game = SteamGame.create(gameData);
                this.games.put(game.getAppId(), game);
                float recent;
                try {
                    recent = Float.parseFloat(gameData.getElementsByTagName("hoursLast2Weeks").item(0).getTextContent());
                } catch(NullPointerException e) {
                    recent = 0;
                }
                float total;
                try {
                    total = Float.parseFloat(gameData.getElementsByTagName("hoursOnRecord").item(0).getTextContent());
                } catch(NullPointerException e) {
                    total = 0;
                }
                int[] playtimes = { (int) (recent * 60), (int) (total * 60) };
                this.playtimes.put(game.getAppId(), playtimes);
            }
        } catch(Exception e) {
            throw new SteamCondenserException("XML data could not be parsed.");
        }
    }

    /**
     * Returns the URL of the full-sized version of this user's avatar
     *
     * @return The URL of the full-sized avatar
     */
    public String getAvatarFullUrl() {
        return this.imageUrl + "_full.jpg";
    }

    /**
     * Returns the URL of the icon version of this user's avatar
     *
     * @return The URL of the icon-sized avatar
     */
    public String getAvatarIconUrl() {
        return this.imageUrl + ".jpg";
    }

    /**
     * Returns the URL of the medium-sized version of this user's avatar
     *
     * @return The URL of the medium-sized avatar
     */
    public String getAvatarMediumUrl() {
        return this.imageUrl + "_medium.jpg";
    }

    /**
     * Returns the base URL for this Steam ID
     * <p>
     * This URL is different for Steam IDs having a custom URL.
     *
     * @return The base URL for this SteamID
     */
    public String getBaseUrl() {
        if(this.customUrl == null) {
            return "http://steamcommunity.com/profiles/" + this.steamId64;
        } else {
            return "http://steamcommunity.com/id/" + this.customUrl;
        }
    }

    /**
     * Returns the custom URL of this Steam ID
     * <p>
     * The custom URL is a user specified unique string that can be used
     * instead of the 64bit SteamID as an identifier for a Steam ID.
     * <p>
     * <strong>Note:</strong> The custom URL is not necessarily the same as the
     * user's nickname.
     *
     * @return The custom URL of this Steam ID
     */
    public String getCustomUrl() {
        return this.customUrl;
    }

    /**
     * Returns the favorite game of this user
     *
     * @deprecated The favorite game is no longer listed for new users
     * @return The favorite game of this user
     */
    public String getFavoriteGame() {
        return this.favoriteGame;
    }

    /**
     * Returns the number of hours that this user played his/her favorite game
     * in the last two weeks
     *
     * @deprecated The favorite game is no longer listed for new users
     * @return The number of hours the favorite game has been played recently
     */
    public float getFavoriteGameHoursPlayed() {
        return this.favoriteGameHoursPlayed;
    }

    /**
     * Returns the time this group has been fetched
     *
     * @return The timestamp of the last fetch time
     */
    public long getFetchTime() {
        return this.fetchTime;
    }

    /**
     * Returns the Steam Community friends of this user
     * <p>
     * If the friends haven't been fetched yet, this is done now.
     *
     * @return The friends of this user
     * @see #fetchFriends
     */
    public SteamId[] getFriends()
            throws SteamCondenserException {
        if(this.friends == null) {
            this.fetchFriends();
        }
        return this.friends;
    }

    /**
     * Returns the games this user owns
     * <p>
     * The keys of the hash are the games' application IDs and the values are
     * the corresponding game instances.
     * <p>
     * If the friends haven't been fetched yet, this is done now.
     *
     * @return array The games this user owns
     * @see #fetchGames
     */
    public HashMap<Integer, SteamGame> getGames()
            throws SteamCondenserException {
        if(this.games == null) {
            this.fetchGames();
        }
        return this.games;
    }

    /**
     * Returns the stats for the given game for the owner of this SteamID
     *
     * @param id The full or short name or the application ID of the game stats
     *        should be fetched for
     * @return The statistics for the game with the given name
     * @throws SteamCondenserException if the user does not own this game or it
     *         does not have any stats
     */
    public GameStats getGameStats(Object id)
            throws SteamCondenserException {
        SteamGame game = this.findGame(id);

        if(!game.hasStats()) {
            throw new SteamCondenserException("\"" + game.getName() + "\" does not have stats.");
        }

        if(this.customUrl == null) {
            return GameStats.createGameStats(this.steamId64, game.getShortName());
        } else {
            return GameStats.createGameStats(this.customUrl, game.getShortName());
        }
    }

    /**
     * Returns the groups this user is a member of
     *
     * @return The groups this user is a member of
     */
    public SteamGroup[] getGroups() {
        return this.groups;
    }

    /**
     * Returns the headline specified by the user
     *
     * @return The headline specified by the user
     */
    public String getHeadLine() {
        return this.headLine;
    }

    /**
     * Returns the number of hours that this user played a game in the last two
     * weeks
     *
     * @return The number of hours the user has played recently
     */
    public float getHoursPlayed() {
        return this.hoursPlayed;
    }

    /**
     * Returns the links that this user has added to his/her Steam ID
     * <p>
     * The keys of the hash contain the titles of the links while the values
     * contain the corresponding URLs.
     *
     * @return The links of this user
     */
    public Map<String, String> getLinks() {
        return this.links;
    }

    /**
     * Returns the location of the user
     *
     * @return The location of the user
     */
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

    /**
     * Returns the Steam nickname of the user
     *
     * @return The Steam nickname of the user
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * Returns the real name of this user
     *
     * @return The real name of this user
     */
    public String getRealName() {
        return this.realName;
    }

    /**
     * Returns the time in minutes this user has played this game in the last
     * two weeks
     *
     * @param id The full or short name or the application ID of the game
     * @return The number of minutes this user played the given game in the
     *         last two weeks
     */
    public int getRecentPlaytime(Object id)
            throws SteamCondenserException {
        SteamGame game = this.findGame(id);

        return this.playtimes.get(game.getAppId())[0];
    }

    /**
     * Returns the message corresponding to this user's online state
     *
     * @return The message corresponding to this user's online state
     * @see #isInGame
     * @see #isOnline
     */
    public String getStateMessage() {
        return this.stateMessage;
    }

    /**
     * Returns this user's 64bit SteamID
     *
     * @return This user's 64bit SteamID
     */
    public long getSteamId64() {
        return this.steamId64;
    }

    /**
     * Returns the Steam rating calculated over the last two weeks' activity
     *
     * @return The Steam rating of this user
     */
    public float getSteamRating() {
        return this.steamRating;
    }

    /**
     * Returns the summary this user has provided
     *
     * @return This user's summary
     */
    public String getSummary() {
        return this.summary;
    }

    /**
     * Returns the total time in minutes this user has played this game
     *
     * @param id The full or short name or the application ID of the game
     * @return The total number of minutes this user played the given game
     */
    public int getTotalPlaytime(Object id)
            throws SteamCondenserException {
        SteamGame game = this.findGame(id);

        return this.playtimes.get(game.getAppId())[1];
    }

    /**
     * Returns the visibility state of this Steam ID
     *
     * @return This Steam ID's visibility State
     */
    public int getVisibilityState() {
        return this.visibilityState;
    }

    /**
     * Tries to find a game instance with the given application ID or full name
     * or short name
     *
     * @param id The full or short name or the application ID of the game
     * @return The game found with the given ID
     * @throws SteamCondenserException if the user does not own the game or no
     *         game with the given ID exists
     */
    private SteamGame findGame(Object id)
            throws SteamCondenserException {
        SteamGame game = null;

        if(id instanceof Integer) {
            game = this.getGames().get(id);
        } else {
            for(SteamGame currentGame : this.getGames().values()) {
                if(id.equals(currentGame.getShortName()) ||
                   id.equals(currentGame.getName())) {
                    game = currentGame;
                    break;
                }
            }
        }

        if(game == null) {
            String message;
            if(id instanceof Integer) {
                message = "This SteamID does not own a game with application ID " + id + ".";
            } else {
                message = "This SteamID does not own the game \"" + id + "\".";
            }
            throw new SteamCondenserException(message);
        }

        return game;
    }

    /**
     * Returns whether the owner of this Steam ID is VAC banned
     *
     * @return <code>true</code> if the user has been banned by VAC
     */
    public boolean isBanned() {
        return this.vacBanned;
    }

    /**
     * Returns whether the data for this Steam ID has already been fetched
     *
     * @return <code>true</code> if the Steam ID's data has been
     *         fetched
     */
    public boolean isFetched() {
        return this.fetchTime != 0;
    }

    /**
    * Returns whether the owner of this Steam ID is playing a game
     *
     * @return <code>true</code> if the user is in-game
     */
    public boolean isInGame() {
        return this.onlineState.equals("in-game");
    }

    /**
     * Returns whether the owner of this Steam ID is currently logged into
     * Steam
     *
     * @return <code>true</code> if the user is online
     */
    public boolean isOnline() {
        return (this.onlineState.equals("online") || this.onlineState.equals("in-game"));
    }
}
