/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2010, Sebastian Staudt
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

	public static SteamGroup create(long id) {
		return SteamGroup.create((Object) id, true, false);
	}

	public static SteamGroup create(String id) {
		return SteamGroup.create((Object) id, true, false);
	}

	public static SteamGroup create(long id, boolean fetch) {
		return SteamGroup.create((Object) id, fetch, false);
	}

	public static SteamGroup create(String id, boolean fetch) {
		return SteamGroup.create((Object) id, fetch, false);
	}

	public static SteamGroup create(long id, boolean fetch, boolean bypassCache) {
		return SteamGroup.create((Object) id, fetch, bypassCache);
	}

	public static SteamGroup create(String id, boolean fetch, boolean bypassCache) {
		return SteamGroup.create((Object) id, fetch, bypassCache);
	}

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

	public static boolean isCached(Object id) {
		return SteamGroup.steamGroups.containsKey(id);
	}

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
	 */
	public String getCustomUrl() {
		return this.customUrl;
	}

	/**
	 * Returns the 64bit group ID
	 */
	public long getGroupId64() {
		return this.groupId64;
	}

	public String getBaseUrl() {
		if(this.customUrl == null) {
			return "http://steamcommunity.com/gid/" + this.groupId64;
		} else {
			return "http://steamcommunity.com/groups/" + this.customUrl;
		}
	}

	public long getFetchTime() {
		return this.fetchTime;
	}

	public long getId() {
		return this.groupId64;
	}

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

	public ArrayList<SteamId> getMembers() {
		if(this.members == null) {
			this.fetchMembers();
		}

		return this.members;
	}

	public boolean isFetched() {
		return this.fetchTime != 0;
	}
}
