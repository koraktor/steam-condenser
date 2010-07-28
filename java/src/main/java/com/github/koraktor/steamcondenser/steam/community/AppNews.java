/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.koraktor.steamcondenser.exceptions.WebApiException;

/**
 * The AppNews class is a representation of Steam news and can be used to load
 * current news about specific games
 *
 * @author Sebastian Staudt
 */
public class AppNews {

    private int appId;

    private String author;

    private String contents;

    private Date date;

    private boolean external;

    private String feedLabel;

    private String feedName;

    private long gid;

    private String title;

    private String url;

    /**
     * Loads the news for the given game with the given restrictions
     *
     * @param appId The unique Steam Application ID of the game (e.g. 440 for
     *              Team Fortress 2). See
     *              http://developer.valvesoftware.com/wiki/Steam_Application_IDs
     *              for all application IDs
     * @return A list of news for the specified game with the given options
     * @throws JSONException If the JSON data cannot be parsed
     * @throws WebApiException If a request to Steam's Web API fails
     */
    public static List<AppNews> getNewsForApp(int appId)
            throws JSONException, WebApiException {
        return getNewsForApp(appId, 5, null);
    }

    /**
     * Loads the news for the given game with the given restrictions
     *
     * @param appId The unique Steam Application ID of the game (e.g. 440 for
     *              Team Fortress 2). See
     *              http://developer.valvesoftware.com/wiki/Steam_Application_IDs
     *              for all application IDs
     * @param count The maximum number of news to load (default: 5). There's no
     *              reliable way to load all news. Use really a really great
     *              number instead
     * @return A list of news for the specified game with the given options
     * @throws JSONException If the JSON data cannot be parsed
     * @throws WebApiException If a request to Steam's Web API fails
     */
    public static List<AppNews> getNewsForApp(int appId, int count)
            throws JSONException, WebApiException {
        return getNewsForApp(appId, count, null);
    }

    /**
     * Loads the news for the given game with the given restrictions
     *
     * @param appId The unique Steam Application ID of the game (e.g. 440 for
     *              Team Fortress 2). See
     *              http://developer.valvesoftware.com/wiki/Steam_Application_IDs
     *              for all application IDs
     * @param count The maximum number of news to load (default: 5). There's no
     *              reliable way to load all news. Use really a really great
     *              number instead
     * @param maxLength The maximum content length of the news (default: nil).
     *                  If a maximum length is defined, the content of the news
     *                  will only be at most <code>maxLength</code> characters
     *                  long plus an ellipsis
     * @return A list of news for the specified game with the given options
     * @throws JSONException If the JSON data cannot be parsed
     * @throws WebApiException If a request to Steam's Web API fails
     */
    public static List<AppNews> getNewsForApp(int appId, int count, Integer maxLength)
            throws JSONException, WebApiException {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("appid", appId);
        params.put("count", count);
        params.put("maxlength", maxLength);
        JSONObject data = new JSONObject(WebApi.getJSON("ISteamNews", "GetNewsForApp", 1, params));

        List<AppNews> newsItems = new ArrayList<AppNews>();
        JSONArray newsData = data.getJSONObject("appnews").getJSONObject("newsitems").getJSONArray("newsitem");
        for(int i = 0; i < newsData.length(); i ++) {
            newsItems.add(new AppNews(appId, newsData.getJSONObject(i)));
        }

        return newsItems;
    }

    /**
     * Creates a new instance of an AppNews news item with the given data
     *
     * @param appId The unique Steam Application ID of the game (e.g. 440 for
     *              Team Fortress 2). See
     *              http://developer.valvesoftware.com/wiki/Steam_Application_IDs
     *              for all application IDs
     * @param newsData The news data extracted from JSON
     * @throws JSONException If the JSON data cannot be parsed
     */
    private AppNews(int appId, JSONObject newsData)
            throws JSONException {
        this.appId     = appId;
        this.author    = newsData.getString("author");
        this.contents  = newsData.getString("contents").trim();
        this.date      = new Date(newsData.getLong("date"));
        this.external  = newsData.getBoolean("is_external_url");
        this.feedLabel = newsData.getString("feedlabel");
        this.feedName  = newsData.getString("feedname");
        this.gid       = newsData.getLong("gid");
        this.title     = newsData.getString("title");
        this.url       = newsData.getString("url");
    }

    /**
     * Returns the unique Application ID of the game this news is about
     *
     * @return The Application ID
     */
    public int getAppId() {
        return this.appId;
    }

    /**
     * Returns the author of this news item
     *
     * @return The author of this news
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * Returns the content of this news item
     * <p>
     * Might be truncated if <code>maxLength</code> is set when using
     * <code>AppNews.getNewsForApp()</code>
     *
     * @return The content of this news
     * @see #getNewsForApp
     */
    public String getContents() {
        return this.contents;
    }

    /**
     * Returns the date this news item has been posted
     *
     * @return The date of this news
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Returns the label of the category this news has been posted in
     *
     * @return The title of this news' category
     */
    public String getFeedLabel() {
        return this.feedLabel;
    }

    /**
     * Returns the name of the category this news has been posted in
     *
     * @return The String identifier of this news' category
     */
    public String getFeedName() {
        return this.feedName;
    }

    /**
     * Returns the global identifier of this news
     *
     * @return The GID of this news
     */
    public long getGid() {
        return this.gid;
    }

    /**
     * Returns the title of this news item
     *
     * @return The title of this news
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * A direct link to the news on the Steam website or a redirecting link to
     * the external post
     *
     * @return The URL to the original news
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * Returns whether this news items originates from a source other than Steam
     * itself (e.g. an external blog)
     *
     * @return Whether this news has been posted externally
     */
    public boolean isExternal() {
        return this.external;
    }

    public String toString() {
        return this.feedLabel + ": " + this.getTitle();
    }

}
