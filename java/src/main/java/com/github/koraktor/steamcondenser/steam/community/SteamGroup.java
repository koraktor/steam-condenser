/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The SteamGroup class represents a group in the Steam Community
 *
 * @author Sebastian Staudt
 */
public class SteamGroup {

	private static Map<Object, SteamGroup> steamGroups = new HashMap<Object, SteamGroup>();

	private String customUrl;

	private long fetchTime;

	private long groupId64;

	private ArrayList<SteamId> members;

    /**
     * Creates a new <code>SteamGroup</code> instance or gets an existing one
     * from the cache for the group with the given ID
     *
     * @param id The 64bit Steam ID of the group
     * @return The <code>SteamGroup</code> instance of the requested group
     */
	public static SteamGroup create(long id) {
		return SteamGroup.create((Object) id, true, false);
	}

    /**
     * Creates a new <code>SteamGroup</code> instance or gets an existing one
     * from the cache for the group with the given ID
     *
     * @param id The custom URL of the group specified by the group admin
     * @return The <code>SteamGroup</code> instance of the requested group
     */
	public static SteamGroup create(String id) {
		return SteamGroup.create((Object) id, true, false);
	}

    /**
     * Creates a new <code>SteamGroup</code> instance or gets an existing one
     * from the cache for the group with the given ID
     *
     * @param id The 64bit Steam ID of the group
     * @param fetch if <code>true</code> the groups's data is loaded into the
     *        object
     * @return The <code>SteamGroup</code> instance of the requested group
     */
	public static SteamGroup create(long id, boolean fetch) {
		return SteamGroup.create((Object) id, fetch, false);
	}

    /**
     * Creates a new <code>SteamGroup</code> instance or gets an existing one
     * from the cache for the group with the given ID
     *
     * @param id The custom URL of the group specified by the group admin
     * @param fetch if <code>true</code> the groups's data is loaded into the
     *        object
     * @return The <code>SteamGroup</code> instance of the requested group
     */
	public static SteamGroup create(String id, boolean fetch) {
		return SteamGroup.create((Object) id, fetch, false);
	}

    /**
     * Creates a new <code>SteamGroup</code> instance or gets an existing one
     * from the cache for the group with the given ID
     *
     * @param id The 64bit Steam ID of the group
     * @param fetch if <code>true</code> the groups's data is loaded into the
     *        object
     * @param bypassCache If <code>true</code> an already cached instance for
     *        this group will be ignored and a new one will be created
     * @return The <code>SteamGroup</code> instance of the requested group
     */
	public static SteamGroup create(long id, boolean fetch, boolean bypassCache) {
		return SteamGroup.create((Object) id, fetch, bypassCache);
	}

    /**
     * Creates a new <code>SteamGroup</code> instance or gets an existing one
     * from the cache for the group with the given ID
     *
     * @param id The custom URL of the group specified by the group admin
     * @param fetch if <code>true</code> the groups's data is loaded into the
     *        object
     * @param bypassCache If <code>true</code> an already cached instance for
     *        this group will be ignored and a new one will be created
     * @return The <code>SteamGroup</code> instance of the requested group
     */
	public static SteamGroup create(String id, boolean fetch, boolean bypassCache) {
		return SteamGroup.create((Object) id, fetch, bypassCache);
	}

    /**
     * Creates a new <code>SteamGroup</code> instance or gets an existing one
     * from the cache for the group with the given ID
     *
     * @param id The custom URL of the group specified by the group admin or
     *        the 64bit group ID
     * @param fetch if <code>true</code> the groups's data is loaded into the
     *        object
     * @param bypassCache If <code>true</code> an already cached instance for
     *        this group will be ignored and a new one will be created
     * @return The <code>SteamGroup</code> instance of the requested group
     */
	private static SteamGroup create(Object id, boolean fetch, boolean bypassCache) {
		if(SteamGroup.isCached(id) && !bypassCache) {
			SteamGroup group = SteamGroup.steamGroups.get(id);
			if(fetch && !group.isFetched()) {
				group.fetchMembers();
			}
			return group;
		} else {
			return new SteamGroup(id, fetch);
		}
	}

    /**
     * Returns whether the requested group is already cached
     *
     * @param id The custom URL of the group specified by the group admin or
     *        the 64bit group ID
     * @return <code>true</code> if this group is already cached
     */
	public static boolean isCached(Object id) {
		return SteamGroup.steamGroups.containsKey(id);
	}

    /**
     * Creates a new <code>SteamGroup</code> instance for the group with the
     * given ID
     *
     * @param id The custom URL of the group specified by the group admin or
     *        the 64bit group ID
     * @param fetch if <code>true</code> the groups's data is loaded into the
     *        object
     */
	private SteamGroup(Object id, boolean fetch) {
		if(id instanceof String) {
			this.customUrl = (String) id;
		} else {
			this.groupId64 = (Long) id;
		}
		
		if(fetch) {
			this.fetchMembers();
		}

		this.cache();
	}

    /**
     * Saves this <code>SteamGroup</code> instance in the cache
     *
     * @return <code>false</code> if this group is already cached
     */
	public boolean cache() {
		if(!SteamGroup.steamGroups.containsKey(this.groupId64)) {
			SteamGroup.steamGroups.put(this.groupId64, this);
			if(this.customUrl != null && !SteamGroup.steamGroups.containsKey(this.customUrl)) {
				SteamGroup.steamGroups.put(this.customUrl, this);
			}
			return true;
		}
		return false;
	}

    /**
     * Loads the members of this group
     * <p>
     * This might take several HTTP requests as the Steam Community splits this
     * data over several XML documents if the group has lots of members.
     */
	public boolean fetchMembers() {
		int page = 0;
		int totalPages;
		String url;
		this.members = new ArrayList<SteamId>();

		try {
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			do {
				page ++;
				url = this.getBaseUrl() + "/memberslistxml?p=" + page;
				Element memberData = parser.parse(url).getDocumentElement();

				totalPages = Integer.parseInt(memberData.getElementsByTagName("totalPages").item(0).getTextContent());

				NodeList membersList = ((Element) memberData.getElementsByTagName("members").item(0)).getElementsByTagName("steamID64");
				for(int i = 0; i < membersList.getLength(); i++) {
					Element member = (Element) membersList.item(i);
					this.members.add(SteamId.create(Long.parseLong(member.getTextContent())));
				}
			} while(page < totalPages);
		} catch(Exception e) {
			return false;
		}

		this.fetchTime = new Date().getTime();

		return true;
	}

    /**
     * Returns the custom URL of this group
     * <p>
     * The custom URL is a admin specified unique string that can be used
     * instead of the 64bit SteamID as an identifier for a group.
     *
     * @return The custom URL of this group
     */
	public String getCustomUrl() {
		return this.customUrl;
	}

    /**
     * Returns this group's 64bit SteamID
     *
     * @return This group's 64bit SteamID
     */
	public long getGroupId64() {
		return this.groupId64;
	}

    /**
     * Returns the base URL for this group's page
     * <p>
     * This URL is different for groups having a custom URL.
     *
     * @return The base URL for this group
     */
	public String getBaseUrl() {
		if(this.customUrl == null) {
			return "http://steamcommunity.com/gid/" + this.groupId64;
		} else {
			return "http://steamcommunity.com/groups/" + this.customUrl;
		}
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
     * Returns this group's 64bit SteamID
     *
     * @return This group's 64bit SteamID
     */
	public long getId() {
		return this.groupId64;
	}

    /**
     * Returns the number of members this group has
     * <p>
     * If the members have already been fetched the size of the member array is
     * returned. Otherwise the group size is separately fetched without needing
     * multiple requests for big groups.
     *
     * @return The number of this group's members
     */
	public int getMemberCount()
		throws ParserConfigurationException, SAXException, IOException {
		if(this.members == null) {
			DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Element memberData = parser.parse(this.getBaseUrl() + "/memberslistxml").getDocumentElement();
			return Integer.parseInt(memberData.getElementsByTagName("memberCount").item(0).getTextContent());
		} else {
			return this.members.size();
		}
	}

    /**
     * Returns the members of this group
     * <p>
     * If the members haven't been fetched yet, this is done now.
     *
     * @return The Steam ID's of the members of this group
     * @see #fetchMembers
     */
	public ArrayList<SteamId> getMembers() {
		if(this.members == null) {
			this.fetchMembers();
		}

		return this.members;
	}

    /**
     * Returns whether the data for this group has already been fetched
     *
     * @return <code>true</code> if the group's members have been
     *         fetched
     */
	public boolean isFetched() {
		return this.fetchTime != 0;
	}
}
