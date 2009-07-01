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

/**
 * The SteamGroup class represents a group in the Steam Community
 * 
 * @author Sebastian Staudt
 */
public class SteamGroup {
	private String customUrl;

	private long groupId64;

	private ArrayList<SteamId> members;

	public SteamGroup(long id) {
		this(id, false);
	}
	
	public SteamGroup(String id) {
		this(id, false);
	}

	public SteamGroup(long id, boolean fetch) {
		this.groupId64 = id;
		
		if (fetch) {
			this.fetchMembers();
		}
	}

	public SteamGroup(String id, boolean fetch) {
		this.customUrl = id;
		
		if (fetch) {
			this.fetchMembers();
		}
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
				
				NodeList membersList = ((Element) memberData.getElementsByTagName("members").item(0)).getElementsByTagName("steamId64");
				for(int i = 0; i < membersList.getLength(); i++) {
					Element member = (Element) membersList.item(i);
					this.members.add(new SteamId(Long.parseLong(member.getTextContent())));
				}
			} while(page < totalPages);
		} catch(Exception e) {
			return false;
		}
		
		return true;
	}
	
	public String getBaseUrl() {
		if(this.customUrl == null) {
			return "http://steamcommunity.com/gid/" + this.groupId64;
		} else {
			return "http://steamcommunity.com/groups/" + this.customUrl;
		}
	}

	public String getCustomUrl() {
		return this.customUrl;
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
}
