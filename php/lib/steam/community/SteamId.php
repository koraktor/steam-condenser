<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 * @version $Id$
 */

require_once "steam/community/GameStats.php";
require_once "steam/community/SteamGroup.php";
require_once "steam/community/tf2/TF2Stats.php";

/**
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class SteamId
{
	/**
	 *
	 * @param $id
	 * @param $fetch
	 * @return unknown_type
	 */
	public function __construct($id, $fetch = true)
	{
		$this->id = $id;

		if($fetch)
		{
			$this->fetchData();
		}
	}

	/**
	 *
	 */
	public function fetchData()
	{
		$profile = new SimpleXMLElement("http://www.steamcommunity.com/id/{$this->id}?xml=1", null, true);

		$this->imageUrl = (string) $profile->avatarIcon;
		$this->onlineState = (string) $profile->onlineState;
		$this->privacyState = (string) $profile->privacyState;
		$this->stateMessage = (string) $profile->stateMessage;
		$this->steamId = (string) $profile->steamID;
		$this->steamId64 = (float) $profile->steamID64;
		$this->vacBanned = (string) $profile->vacBanned == "1";
		$this->visibilityState = intval((string) $profile->visibilityState);

		if($this->privacyState == "public")
		{
			$this->customURL = (string) $profile->customURL;
			$this->favoriteGame = (string) $profile->favoriteGame->name;
			$this->favoriteGameHoursPlayed = (string) $profile->favoriteGame->hoursPlayed2wk;
			$this->headLine = (string) $profile->headline;
			$this->hoursPlayed = floatval((string) $profile->hoursPlayed2Wk);
			$this->location = (string) $profile->location;
			$this->memberSince = (string) $profile->memberSince;
			$this->realName = (string) $profile->realname;
			$steamRating = explode(" - ", (string) $profile->steamRating);
			$this->steamRating = $steamRating[0];
			$this->steamRatingText = $steamRating[1];
			$this->summary = (string) $profile->summary;
		}

		foreach($profile->mostPlayedGames->mostPlayedGame as $mostPlayedGame)
		{
			$this->mostPlayedGames[(string) $mostPlayedGame->gameName] = floatval((string) $mostPlayedGame->hoursPlayed);
		}

		foreach($profile->friends->friend as $friend)
		{
			$this->friends[] = new SteamId(intval((string) $friend->steamID64), false);
		}

		foreach($profile->groups->group as $group)
		{
			$this->groups[] = new SteamGroup(intval((string) $group->groupID64));
		}

		foreach($profile->weblinks->weblink as $link)
		{
			$this->links[(string) $link->title] = (string) $link->link;
		}
	}

	/**
	 *
	 * @return String
	 */
	public function getFullAvatarUrl()
	{
		return $this->imageUrl . "_full.jpg";
	}

	/**
	 *
	 * @param $gameName
	 * @return GameStats
	 */
	public function getGameStats($gameName)
	{
		if($gameName == "TF2")
		{
			return new TF2Stats($this->customUrl);
		}
		else
		{
			return new GameStats($this->customUrl, $gameName);
		}
	}

	/**
	 *
	 * @return String
	 */
	public function getIconAvatarUrl()
	{
		return $this->imageUrl . "_.jpg";
	}

	/**
	 * Returns the URL of the medium version of this user's avatar
	 * @return String
	 */
	public function getMediumAvatarUrl()
	{
		return $this->imageUrl . "_medium.jpg";
	}

	/**
	 * Returns whether the owner of this SteamID is VAC banned
	 * @return boolean
	 */
	public function isBanned()
	{
		return $this->vacBanned;
	}

	/**
	 * Returns whether the owner of this SteamId is playing a game
	 * @return boolean
	 */
	public function isInGame()
	{
		return $this->onlineState == "in-game";
	}

	/**
	 * Returns whether the owner of this SteamID is currently logged into Steam
	 * @return boolean
	 */
	public function isOnline()
	{
		return ($this->onlineState == "online") || ($this->onlineState == "in-game");
	}
}
?>