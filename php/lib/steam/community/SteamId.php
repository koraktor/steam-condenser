<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2010, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage Steam Community
 */

require_once STEAM_CONDENSER_PATH . 'exceptions/SteamCondenserException.php';
require_once STEAM_CONDENSER_PATH . 'steam/community/GameStats.php';
require_once STEAM_CONDENSER_PATH . 'steam/community/SteamGroup.php';

/**
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class SteamId {

    /**
     * @var array
     */
    public static $steamIds = array();

    /**
     * @var String
     */
    private $customUrl;

    /**
     * @var int
     */
    private $fetchTime;

    /**
     * @var array
     */
    private $friends;

    /**
     * @var array
     */
    private $games;

    /**
     * @var String
     */
    private $nickname;

    /**
     * @var String
     */
    private $steamId64;

    /**
     * Returns whether the requested SteamID is already cached
     * @param String id
     */
    public static function isCached($id) {
        return array_key_exists(strtolower($id), self::$steamIds);
    }

    /**
     * Clears the group cache
     */
    public static function clearCache() {
        self::$steamIds = array();
    }

    /**
     * Converts the 64bit SteamID as used and reported by the Steam Community
     * to a SteamID reported by game servers
     * @return String
     */
    public static function convertCommunityIdToSteamId($communityId) {
        $steamId1  = substr($communityId, -1) % 2;
        $steamId2a = intval(substr($communityId, 0, 4)) - 7656;
        $steamId2b = substr($communityId, 4) - 1197960265728;
        $steamId2b = $steamId2b - $steamId1;

        if($steamId2a <= 0 && $steamId2b <= 0) {
            throw new SteamCondenserException("SteamID $communityId is too small.");
        }

        return "STEAM_0:$steamId1:" . (($steamId2a + $steamId2b) / 2);
    }

    /**
     * Converts the SteamID as reported by game servers to a 64bit SteamID
     * @return String
     */
    public static function convertSteamIdToCommunityId($steamId) {
        if($steamId == 'STEAM_ID_LAN' || $steamId == 'BOT') {
            throw new SteamCondenserException("Cannot convert SteamID \"$steamId\" to a community ID.");
        }
        if(preg_match('/^STEAM_[0-1]:[0-1]:[0-9]+$/', $steamId) == 0) {
            throw new SteamCondenserException("SteamID \"$steamId\" doesn't have the correct format.");
        }

        $steamId = explode(':', substr($steamId, 6));
        $steamId = $steamId[1] + $steamId[2] * 2 + 1197960265728;

        return '7656' . $steamId;
    }

    /**
     * This checks the cache for an existing SteamID. If it exists it is returned.
     * Otherwise a new SteamID is created.
     * @param String $id
     * @param boolean $fetch
     * @param boolean $bypassCache
     * @return SteamId
     */
    public static function create($id, $fetch = true, $bypassCache = false) {
        $id = strtolower($id);
        if(self::isCached($id) && !$bypassCache) {
            $steamId = self::$steamIds[$id];
            if($fetch && !$steamId->isFetched()) {
                $steamId->fetchMembers();
            }
            return $steamId;
        } else {
            return new SteamId($id, $fetch);
        }
    }

    /**
     * Creates a new SteamId object using the SteamID64 converted from a server
     * SteamID given by +steam_id+
     */
    public static function getFromSteamId($steamId) {
        return new SteamId(self::convertSteamIdToCommunityId($steamId));
    }

    /**
     * Creates a new SteamId object for the given SteamID, either numeric or the
     * custom URL specified by the user. If fetch is true, fetch_data is used to
     * load data into the object. fetch defaults to true.
     * Due to restrictions in PHP's integer representation you have to use
     * String representation for numeric IDs also, e.g. '76561197961384956'.
     * @param String $id
     * @param boolean $fetch
     */
    public function __construct($id, $fetch = true) {
        if(is_numeric($id)) {
            $this->steamId64 = $id;
        }
        else {
            $this->customUrl = strtolower($id);
        }

        if($fetch) {
            $this->fetchData();
        }

        $this->cache();
    }

    /**
     * Saves this SteamID in the cache
     */
    public function cache() {
        if(!array_key_exists($this->steamId64, self::$steamIds)) {
            self::$steamIds[$this->steamId64] = $this;
            if(!empty($this->customUrl) &&
               !array_key_exists($this->customUrl, self::$steamIds)) {
               self::$steamIds[$this->customUrl] = $this;
            }
        }
    }

    /**
     * Fetchs data from the Steam Community by querying the XML version of the
     * profile specified by the ID of this SteamID
     */
    public function fetchData() {
        $url = $this->getBaseUrl() . "?xml=1";
        $profile = new SimpleXMLElement(file_get_contents($url));

        if(!empty($profile->error)) {
            throw new SteamCondenserException((string) $profile->error);
        }

        $this->nickname  = (string) $profile->steamID;
        $this->steamId64 = (string) $profile->steamID64;
        $this->vacBanned = (bool) $profile->vacBanned;

        if(!empty($profile->privacyMessage)) {
            throw new SteamCondenserException((string) $profile->privacyMessage);
        }

        $this->imageUrl = substr((string) $profile->avatarIcon, 0, -4);
        $this->onlineState = (string) $profile->onlineState;
        $this->privacyState = (string) $profile->privacyState;
        $this->stateMessage = (string) $profile->stateMessage;
        $this->visibilityState = (int) $profile->visibilityState;

        if($this->privacyState == "public") {
            $this->customUrl = strtolower((string) $profile->customURL);
            $this->favoriteGame = (string) $profile->favoriteGame->name;
            $this->favoriteGameHoursPlayed = (string) $profile->favoriteGame->hoursPlayed2wk;
            $this->headLine = (string) $profile->headline;
            $this->hoursPlayed = (float) $profile->hoursPlayed2Wk;
            $this->location = (string) $profile->location;
            $this->memberSince = (string) $profile->memberSince;
            $this->realName = (string) $profile->realname;
            $this->steamRating = (float) $profile->steamRating;
            $this->summary = (string) $profile->summary;
        }

        if(!empty($profile->mostPlayedGames)) {
            foreach($profile->mostPlayedGames->mostPlayedGame as $mostPlayedGame) {
                $this->mostPlayedGames[(string) $mostPlayedGame->gameName] = (float) $mostPlayedGame->hoursPlayed;
            }
        }

        if(!empty($profile->groups)) {
            foreach($profile->groups->group as $group) {
                $this->groups[] = SteamGroup::create((string) $group->groupID64, false);
            }
        }

        if(!empty($profile->weblinks)) {
            foreach($profile->weblinks->weblink as $link) {
                $this->links[(string) $link->title] = (string) $link->link;
            }
        }

        $this->fetchTime = time();
    }

    /**
     * Fetches the friends of this user
     */
    private function fetchFriends() {
        $url = $this->getBaseUrl() . '/friends?xml=1';

        $this->friends = array();
        $friendsData =  new SimpleXMLElement(file_get_contents($url));
        foreach($friendsData->friends->friend as $friend) {
            $this->friends[] = SteamId::create((string) $friend, false);
        }
    }

    /**
     * Fetches the games this user owns
     */
    private function fetchGames() {
        $this->games = array();

        $url = $this->getBaseUrl() . "/games?xml=1";
        $gamesData = new SimpleXMLElement(file_get_contents($url));

        foreach($gamesData->games->game as $game) {
            $gameName = (string) $game->name;
            if($game->globalStatsLink != null) {
                preg_match('#http://steamcommunity.com/stats/([^?/]+)/achievements/#', (string) $game->globalStatsLink, $friendlyName);
                $this->games[$gameName] = strtolower($friendlyName[1]);
            } else {
                $this->games[$gameName] = false;
            }
        }
    }

    /**
     * @return String
     */
    public function getBaseUrl() {
        if(empty($this->customUrl)) {
            return "http://steamcommunity.com/profiles/{$this->steamId64}";
        }
        else {
            return "http://steamcommunity.com/id/{$this->customUrl}";
        }
    }

    /**
     * Return the time the data of this SteamId has been fetched
     * @return int
     */
    public function getFetchTime() {
        return $this->fetchTime;
    }

    /**
     * Returns an array of SteamId representing all Steam Community friends of
     * this user.
     * @return array
     */
    public function getFriends() {
        if(empty($this->friends)) {
            $this->fetchFriends();
        }
        return $this->friends;
    }

    /**
     *
     * @return String
     */
    public function getFullAvatarUrl() {
        return $this->imageUrl . "_full.jpg";
    }

    /**
     * Returns a associative array with the games this user owns. The keys are
     * the games' names and the values are the "friendly names" used for stats
     * or false if the games has no stats.
     * @return array
     */
    public function getGames() {
        if(empty($this->games)) {
            $this->fetchGames();
        }

        return $this->games;
    }

    /**
     * Returns a GameStats object for the given game for the owner of this SteamID
     *
     * @param $gameName
     * @return GameStats
     */
    public function getGameStats($gameName) {
        if(in_array($gameName, array_values($this->getGames()))) {
            $friendlyName = $gameName;
        }
        else if(array_key_exists(strtolower($gameName), $this->getGames())) {
            $friendlyName = $this->games[strtolower($gameName)];
        }
        else {
            throw new SteamCondenserException("Stats for game {$gameName} do not exist.");
        }

        if(empty($this->customUrl)) {
            return GameStats::createGameStats($this->steamId64, $friendlyName);
        }
        else {
            return GameStats::createGameStats($this->customUrl, $friendlyName);
        }
    }

    /**
     *
     * @return String
     */
    public function getIconAvatarUrl() {
        return $this->imageUrl . ".jpg";
    }

    /**
     * Returns the URL of the medium version of this user's avatar
     * @return String
     */
    public function getMediumAvatarUrl() {
        return $this->imageUrl . "_medium.jpg";
    }

    /**
     * @return String
     */
    public function getNickname() {
        return $this->nickname;
    }

    /**
     * Returns whether the owner of this SteamID is VAC banned
     * @return boolean
     */
    public function isBanned() {
        return $this->vacBanned;
    }

    /**
     * Returns whether the data for this SteamID has already been fetched
     * @return boolean
     */
    public function isFetched() {
        return !empty($this->fetchTime);
    }

    /**
     * Returns whether the owner of this SteamId is playing a game
     * @return boolean
     */
    public function isInGame() {
        return $this->onlineState == "in-game";
    }

    /**
     * Returns whether the owner of this SteamID is currently logged into Steam
     * @return boolean
     */
    public function isOnline() {
        return ($this->onlineState == "online") || ($this->onlineState == "in-game");
    }
}
?>
