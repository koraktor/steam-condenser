/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser.steam.community;

import org.junit.Test;

import com.github.koraktor.steamcondenser.steam.community.tf2.TF2Stats;

/**
 * @author Sebastian Staudt
 */
public class SteamCommunityTests {
	@Test
	public void getGames() throws Exception {
		SteamId steamId = SteamId.create("koraktor");
		steamId.getGames();
	}

	@Test
	public void groupByCustomUrl() throws Exception {
		SteamGroup group = SteamGroup.create("valve");
		System.out.println(group.getMemberCount());
	}

	@Test
	public void groupByGroupId64() throws Exception {
		SteamGroup group = SteamGroup.create(103582791429521412L);
		System.out.println(group.getMemberCount());
	}

	/**
	 * This test tries to aquire information from a online Steam ID. This test
	 * only passes if the parsing of the XML document works
	 *
	 * @throws DOMException
	 * @throws IOException
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	@Test
	public void steamIdByCustomUrl() throws Exception {
		SteamId steamId = SteamId.create("koraktor");
		System.out.println(steamId.getSteamId64());
		TF2Stats tf2Stats = (TF2Stats) steamId.getGameStats("tf2");
		System.out.println(tf2Stats.getClassStats().get(0).getName());
	}

	/**
	 * This test tries to aquire information from a online Steam ID. This test
	 * only passes if the parsing of the XML document works
	 *
	 * @throws DOMException
	 * @throws IOException
	 * @throws ParseException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	@Test
	public void steamIdBySteamId64() throws Exception {
		SteamId steamId = SteamId.create(76561197961384956L);
		System.out.println(steamId.getSteamId64());
		System.out.println(steamId.getLocation());
		TF2Stats tf2Stats = (TF2Stats) steamId.getGameStats("tf2");
		System.out.println(tf2Stats.getClassStats().get(0).getName());
	}
}
