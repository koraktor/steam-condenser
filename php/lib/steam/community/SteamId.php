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
        if(is_numeric($id)) {
            $this->steamId64 = $id;
        }
        else {
            $this->customUrl = $id;
        }

        if($fetch) {
            $this->fetchData();
        }
    }

    /**
     *
     */
    public function fetchData()
    {
        if(empty($this->customUrl)) {
            $url = "http://steamcommunity.com/profiles/{$this->steamId64}?xml=1";
        }
        else {
            $url = "http://steamcommunity.com/id/{$this->customUrl}?xml=1";
        }

        $headers = get_headers($url);
        if($headers["Location"] != $url) {
            $url = $headers["Location"] . "?xml=1";
        }

        $profile = new SimpleXMLElement($url, null, true);

        $this->imageUrl = (string) $profile->avatarIcon;
        $this->onlineState = (string) $profile->onlineState;
        $this->privacyState = (string) $profile->privacyState;
        $this->stateMessage = (string) $profile->stateMessage;
        $this->steamId = (string) $profile->steamID;
        $this->steamId64 = (float) $profile->steamID64;
        $this->vacBanned = (string) $profile->vacBanned == "1";
        $this->visibilityState = intval((string) $profile->visibilityState);

        if($this->privacyState == "public") {
            $this->customURL = (string) $profile->customURL;
            $this->favoriteGame = (string) $profile->favoriteGame->name;
            $this->favoriteGameHoursPlayed = (string) $profile->favoriteGame->hoursPlayed2wk;
            $this->headLine = (string) $profile->headline;
            $this->hoursPlayed = floatval((string) $profile->hoursPlayed2Wk);
            $this->location = (string) $profile->location;
            $this->memberSince = (string) $profile->memberSince;
            $this->realName = (string) $profile->realname;
            $this->steamRating = floatval((string) $profile->steamRating);
            $this->summary = (string) $profile->summary;
        }

        foreach($profile->mostPlayedGames->mostPlayedGame as $mostPlayedGame) {
            $this->mostPlayedGames[(string) $mostPlayedGame->gameName] = floatval((string) $mostPlayedGame->hoursPlayed);
        }

        foreach($profile->friends->friend as $friend) {
            $this->friends[] = new SteamId(intval((string) $friend->steamID64), false);
        }

        foreach($profile->groups->group as $group) {
            $this->groups[] = new SteamGroup(intval((string) $group->groupID64));
        }

        foreach($profile->weblinks->weblink as $link) {
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
        if(empty($this->customUrl)) {
            return new GameStats($this->steamId64, $gameName);
        }
        else {
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