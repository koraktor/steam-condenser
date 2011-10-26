/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.github.koraktor.steamcondenser.exceptions.SteamCondenserException;

/**
 * The GameLeaderboard class represents a single leaderboard for a specific
 * game
 *
 * @author Sebastian Staudt
 */
public class GameLeaderboard {

    public static final int LEADERBOARD_DISPLAY_TYPE_NONE         = 0;
    public static final int LEADERBOARD_DISPLAY_TYPE_NUMERIC      = 1;
    public static final int LEADERBOARD_DISPLAY_TYPE_SECONDS      = 2;
    public static final int LEADERBOARD_DISPLAY_TYPE_MILLISECONDS = 3;

    public static final int LEADERBOARD_SORT_METHOD_NONE = 0;
    public static final int LEADERBOARD_SORT_METHOD_ASC  = 1;
    public static final int LEADERBOARD_SORT_METHOD_DESC = 2;

    private static Map<String, Map<Integer, GameLeaderboard>> leaderboards = new HashMap<String, Map<Integer, GameLeaderboard>>();

    protected int id;

    protected String url;

    protected String name;

    protected int entryCount;

    protected int sortMethod;

    protected int displayType;

    /**
     * Returns the leaderboard for the given game and leaderboard ID or name
     *
     * @param gameName The short name of the game
     * @param id The ID of the leaderboard to return
     * @return The matching leaderboard if available
     */
    public static GameLeaderboard getLeaderboard(String gameName, int id)
            throws SteamCondenserException {
        return getLeaderboards(gameName).get(id);
    }

    /**
     * Returns the leaderboard for the given game and leaderboard ID or name
     *
     * @param gameName The short name of the game
     * @param id The name of the leaderboard to return
     * @return The matching leaderboard if available
     */
    public static GameLeaderboard getLeaderboard(String gameName, String name)
            throws SteamCondenserException {
        Map<Integer, GameLeaderboard> leaderboards = getLeaderboards(gameName);

        for(GameLeaderboard board : leaderboards.values()) {
            if(board.getName().equals(name)) {
                return board;
            }
        }

        return null;
    }

    /**
     * Returns an array containing all of a game's leaderboards
     *
     * @param gameName The name of the game
     * @return The leaderboards for this game
     */
    public static Map<Integer, GameLeaderboard> getLeaderboards(String gameName)
            throws SteamCondenserException {
        if(!leaderboards.containsKey(gameName)) {
            loadLeaderboards(gameName);
        }

        return leaderboards.get(gameName);
    }

    /**
     * Loads the leaderboards of the specified games into the cache
     *
     * @param gameName The short name of the game
     * @throws SteamCondenserException if an error occurs while fetching the
     *         leaderboards
     */
    private static void loadLeaderboards(String gameName)
            throws SteamCondenserException {
        String url = String.format("http://steamcommunity.com/stats/%s/leaderboards/?xml=1", gameName);
        try {
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Element boardsData = parser.parse(url).getDocumentElement();

            NodeList errorNode = boardsData.getElementsByTagName("error");
            if(errorNode.getLength() > 0) {
                throw new SteamCondenserException(errorNode.item(0).getTextContent());
            }

            leaderboards.put(gameName, new HashMap<Integer, GameLeaderboard>());
            NodeList boardsList = boardsData.getElementsByTagName("leaderboard");
            for(int i = 0; i < boardsList.getLength(); i++) {
                Element boardData = (Element) boardsList.item(i);
                GameLeaderboard leaderboard = new GameLeaderboard(boardData);
                leaderboards.get(gameName).put(leaderboard.getId(), leaderboard);
            }
        } catch(Exception e) {
            throw new SteamCondenserException("XML data could not be parsed.", e);
        }
    }

    /**
     * Creates a new leaderboard instance with the given XML data
     *
     * @param boardData The XML data of the leaderboard
     */
    private GameLeaderboard(Element boardData) {
        this.url         = boardData.getElementsByTagName("url").item(0).getTextContent();
        this.id          = Integer.parseInt(boardData.getElementsByTagName("lbid").item(0).getTextContent());
        this.name        = boardData.getElementsByTagName("name").item(0).getTextContent();
        this.entryCount  = Integer.parseInt(boardData.getElementsByTagName("entries").item(0).getTextContent());
        this.sortMethod  = Integer.parseInt(boardData.getElementsByTagName("sortmethod").item(0).getTextContent());
        this.displayType = Integer.parseInt(boardData.getElementsByTagName("displaytype").item(0).getTextContent());
    }

    /**
     * Returns the display type of the scores on this leaderboard
     *
     * @return The display type of the scores
     */
    public int getDisplayType() {
        return this.displayType;
    }


    /**
     * Returns the number of entries on this leaderboard
     *
     * @return The number of entries on this leaderboard
     */
    public int getEntryCount() {
        return this.entryCount;
    }

    /**
     * Returns the ID of the leaderboard
     *
     * @return The ID of the leaderboard
     */
    public int getId() {
        return this.id;
    }


    /**
     * Returns the name of the leaderboard
     *
     * @return The name of the leaderboard
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the method that is used to sort the entries on the leaderboard
     *
     * @return The sort method
     */
    public int getSortMethod() {
        return this.sortMethod;
    }

    /**
     * Returns the entry on this leaderboard for the user with the given
     * SteamID
     *
     * @param steamId The <code>SteamId</code> object of the user
     * @return The entry of the user if available
     */
    public GameLeaderboardEntry getEntryForSteamId(SteamId steamId)
            throws SteamCondenserException {
        return this.getEntryForSteamId(steamId.getSteamId64());
    }

    /**
     * Returns the entry on this leaderboard for the user with the given
     * SteamID
     *
     * @param steamId The 64bit SteamID of the user
     * @return The entry of the user if available
     */
    public GameLeaderboardEntry getEntryForSteamId(long steamId)
            throws SteamCondenserException {
        String url = String.format("%s&steamid=%s", this.url, steamId);
        try {
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Element boardData = parser.parse(url).getDocumentElement();

            NodeList errorNode = boardData.getElementsByTagName("error");
            if(errorNode.getLength() > 0) {
                throw new SteamCondenserException(errorNode.item(0).getTextContent());
            }

            NodeList entryList = ((Element) boardData.getElementsByTagName("entries").item(0)).getElementsByTagName("entry");
            for(int i = 0; i < entryList.getLength(); i++) {
                Element entryData = (Element) entryList.item(i);
                if(Long.parseLong(entryData.getElementsByTagName("steamid").item(0).getTextContent()) == steamId) {
                    return new GameLeaderboardEntry(entryData, this);
                }
            }

            return null;
        } catch(Exception e) {
            throw new SteamCondenserException("XML data could not be parsed.", e);
        }
    }

    /**
     * Returns an array of entries on this leaderboard for the user with the
     * given SteamID and his/her friends
     *
     * @param steamId The <code>SteamId</code> object of the user
     * @return The entries of the user and his/her friends
     */
    public Map<Integer, GameLeaderboardEntry> getEntryForSteamIdFriends(SteamId steamId)
            throws SteamCondenserException {
        return this.getEntryForSteamIdFriends(steamId.getSteamId64());
    }

    /**
     * Returns an array of entries on this leaderboard for the user with the
     * given SteamID and his/her friends
     *
     * @param steamId The 64bit SteamID or the <var>SteamId</var> object
     *        of the user
     * @return The entries of the user and his/her friends
     */
    public Map<Integer, GameLeaderboardEntry> getEntryForSteamIdFriends(long steamId)
            throws SteamCondenserException {
        String url = String.format("%s&steamid=%s", this.url, steamId);
        try {
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Element boardData = parser.parse(url).getDocumentElement();

            NodeList errorNode = boardData.getElementsByTagName("error");
            if(errorNode.getLength() > 0) {
                throw new SteamCondenserException(errorNode.item(0).getTextContent());
            }

            Map<Integer, GameLeaderboardEntry> entries = new HashMap<Integer, GameLeaderboardEntry>();
            NodeList entryList = ((Element) boardData.getElementsByTagName("entries").item(0)).getElementsByTagName("entry");
            for(int i = 0; i < entryList.getLength(); i++) {
                Element entryData = (Element) entryList.item(i);
                GameLeaderboardEntry entry = new GameLeaderboardEntry(entryData, this);
                entries.put(entry.getRank(), entry);
            }

            return entries;
        } catch(Exception e) {
            throw new SteamCondenserException("XML data could not be parsed.", e);
        }
    }

    /**
     * Returns the entries on this leaderboard for a given rank range
     *
     * The range is inclusive and a maximum of 5001 entries can be returned in
     * a single request.
     *
     * @param first The first entry to return from the leaderboard
     * @param last The last entry to return from the leaderboard
     * @return The entries that match the given rank range
     */
    public Map<Integer, GameLeaderboardEntry> getEntryRange(int first, int last)
            throws SteamCondenserException {
        if(last < first) {
            throw new SteamCondenserException("First entry must be prior to last entry for leaderboard entry lookup.");
        }
        if((last - first) > 5000) {
            throw new SteamCondenserException("Leaderboard entry lookup is currently limited to a maximum of 5001 entries per request.");
        }

        String url = String.format("%s&start=%d&end=%d", this.url, first, last);
        try {
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Element boardData = parser.parse(url).getDocumentElement();

            NodeList errorNode = boardData.getElementsByTagName("error");
            if(errorNode.getLength() > 0) {
                throw new SteamCondenserException(errorNode.item(0).getTextContent());
            }

            Map<Integer, GameLeaderboardEntry> entries = new HashMap<Integer, GameLeaderboardEntry>();
            NodeList entryList = ((Element) boardData.getElementsByTagName("entries").item(0)).getElementsByTagName("entry");
            for(int i = 0; i < entryList.getLength(); i++) {
                Element entryData = (Element) entryList.item(i);
                GameLeaderboardEntry entry = new GameLeaderboardEntry(entryData, this);
                entries.put(entry.getRank(), entry);
            }

            return entries;
        } catch(Exception e) {
            throw new SteamCondenserException("XML data could not be parsed.", e);
        }
    }
}
