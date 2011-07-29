<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 *
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 */

require_once STEAM_CONDENSER_PATH . 'exceptions/SteamCondenserException.php';
require_once STEAM_CONDENSER_PATH . 'steam/community/GameStats.php';
require_once STEAM_CONDENSER_PATH . 'steam/community/SteamGame.php';
require_once STEAM_CONDENSER_PATH . 'steam/community/SteamGroup.php';

/**
 * The SteamId class represents a Steam Community profile (also called Steam
 * ID)
 *
 * @author     Sebastian Staudt
 * @package    steam-condenser
 * @subpackage community
 */
class SteamId {

    /**
     * @var array
     */
    private static $steamIds = array();

    /**
     * @var string
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
     * @var string
     */
    private $nickname;

    /**
     * @var array
     */
    private $playtimes;

    /**
     * @var string
     */
    private $steamId64;

    /**
     * Returns whether the requested Steam ID is already cached
     *
     * @param string $id The custom URL of the Steam ID specified by the player
     *        or the 64bit SteamID
     * @return <var>true</var> if this Steam ID is already cached
     */
    public static function isCached($id) {
        return array_key_exists(strtolower($id), self::$steamIds);
    }

    /**
     * Clears the Steam ID cache
     */
    public static function clearCache() {
        self::$steamIds = array();
    }

    /**
     * Converts a 64bit numeric SteamID as used by the Steam Community to a
     * SteamID as reported by game servers
     *
     * @param string $communityId The SteamID string as used by the Steam
     *        Community
     * @return string The converted SteamID, like <var>STEAM_0:0:12345</var>
     * @throws SteamCondenserException if the community ID is to small
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
     * Converts a SteamID as reported by game servers to a 64bit numeric
     * SteamID as used by the Steam Community
     *
     * @param string $steamId The SteamID string as used on servers, like
     *        <var>STEAM_0:0:12345</var>
     * @return string The converted 64bit numeric SteamID
     * @throws SteamCondenserException if the SteamID doesn't have the correct
     *         format
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
     * Creates a new <var>SteamID</var> instance or gets an existing one from
     * the cache for the profile with the given ID
     *
     * @param string $id The custom URL of the Steam ID specified by player or
     *        the 64bit SteamID
     * @param boolean $fetch if <var>true</var> the profile's data is loaded
     *        into the object
     * @param boolean $bypassCache If <var>true</var> an already cached
     *        instance for this Steam ID will be ignored and a new one will be
     *        created
     * @return SteamId The <var>SteamId</var> instance of the requested profile
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
     * Creates a new <var>SteamId</var> instance using a SteamID as used on
     * servers
     *
     * The SteamID from the server is converted into a 64bit numeric SteamID
     * first before this is used to retrieve the corresponding Steam Community
     * profile.
     *
     * @param string $steamId The SteamID string as used on servers, like
     *        <var>STEAM_0:0:12345</var>
     * @return SteamId The <var>SteamId</var> instance belonging to the given
     *         SteamID
     * @see convertSteamIdToCommunityId()
     * @see __construct()
     */
    public static function getFromSteamId($steamId) {
        return new SteamId(self::convertSteamIdToCommunityId($steamId));
    }

    /**
     * Creates a new <var>SteamId</var> instance for the given ID
     *
     * @param string $id The custom URL of the group specified by the player
     *        or the 64bit SteamID
     * @param boolean $fetch if <var>true</var> the profile's data is loaded
     *        into the object
     * @throws SteamCondenserException if the Steam ID data is not available,
     *         e.g. when it is private
     */
    public function __construct($id, $fetch = true) {
        if(is_numeric($id)) {
            $this->steamId64 = $id;
        } else {
            $this->customUrl = strtolower($id);
        }

        if($fetch) {
            $this->fetchData();
        }

        $this->cache();
    }

    /**
     * Saves this <var>SteamId</var> instance in the cache
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
     * profile specified by the ID of this Steam ID
     *
     * @throws SteamCondenserException if the Steam ID data is not available,
     *         e.g. when it is private, or when it cannot be parsed
     */
    public function fetchData() {
        $url = $this->getBaseUrl() . '?xml=1';
        $profile = new SimpleXMLElement(file_get_contents($url));

        if(!empty($profile->error)) {
            throw new SteamCondenserException((string) $profile->error);
        }

        $this->nickname  = htmlspecialchars_decode((string) $profile->steamID);
        $this->steamId64 = (string) $profile->steamID64;
        $this->vacBanned = (bool)(int) $profile->vacBanned;

        if(!empty($profile->privacyMessage)) {
            throw new SteamCondenserException((string) $profile->privacyMessage);
        }

        $this->imageUrl = substr((string) $profile->avatarIcon, 0, -4);
        $this->onlineState = (string) $profile->onlineState;
        $this->privacyState = (string) $profile->privacyState;
        $this->stateMessage = (string) $profile->stateMessage;
        $this->visibilityState = (int) $profile->visibilityState;

        if($this->privacyState == 'public') {
            $this->customUrl = strtolower((string) $profile->customURL);
            $this->favoriteGame = (string) $profile->favoriteGame->name;
            $this->favoriteGameHoursPlayed = (string) $profile->favoriteGame->hoursPlayed2wk;
            $this->headLine = htmlspecialchars_decode((string) $profile->headline);
            $this->hoursPlayed = (float) $profile->hoursPlayed2Wk;
            $this->location = (string) $profile->location;
            $this->memberSince = (string) $profile->memberSince;
            $this->realName = htmlspecialchars_decode((string) $profile->realname);
            $this->steamRating = (float) $profile->steamRating;
            $this->summary = htmlspecialchars_decode((string) $profile->summary);
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
                $this->links[htmlspecialchars_decode((string) $link->title)] = (string) $link->link;
            }
        }

        $this->fetchTime = time();
    }

    /**
     * Fetches the friends of this user
     *
     * This creates a new <var>SteamId</var> instance for each of the friends
     * without fetching their data.
     *
     * @see getFriends()
     * @see __construct()
     * @throws SteamCondenserException if an error occurs while parsing the
     *         data
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
     *
     * @see getGames()
     * @throws SteamCondenserException if an error occurs while parsing the
     *         data
     */
    private function fetchGames() {
        $this->games = array();
        $this->playtimes = array();

        $url = $this->getBaseUrl() . '/games?xml=1';
        $gamesData = new SimpleXMLElement(file_get_contents($url));

        foreach($gamesData->games->game as $gameData) {
            $game = SteamGame::create($gameData);
            $this->games[$game->getAppId()] = $game;
            $recent = (float) $gameData->hoursLast2Weeks;
            $total = (float) $gameData->hoursOnRecord;
            $playtimes = array((int) ($recent * 60), (int) ($total * 60));
            $this->playtimes[$game->getAppId()] = $playtimes;
        }
    }

    /**
     * Tries to find a game instance with the given application ID or full name
     * or short name
     *
     * @param mixed $id The full or short name or the application ID of the
     *        game
     * @return SteamGame The game found with the given ID
     * @throws SteamCondenserException if the user does not own the game or no
     *         game with the given ID exists
     */
    private function findGame($id) {
        $game = null;
        foreach($this->getGames() as $currentGame) {
            if($currentGame->getAppId() == $id ||
               $currentGame->getShortName() == $id ||
               $currentGame->getName() == $id) {
                $game = $currentGame;
                break;
            }
        }

        if($game == null) {
            if(is_int($id)) {
                $message = "This SteamID does not own a game with application ID {$id}.";
            } else {
                $message = "This SteamID does not own the game \"{$id}\".";
            }
            throw new SteamCondenserException($message);
        }

        return $game;
    }

    /**
     * Returns the base URL for this Steam ID
     *
     * This URL is different for Steam IDs having a custom URL.
     *
     * @return string The base URL for this SteamID
     */
    public function getBaseUrl() {
        if(empty($this->customUrl)) {
            return "http://steamcommunity.com/profiles/{$this->steamId64}";
        } else {
            return "http://steamcommunity.com/id/{$this->customUrl}";
        }
    }

    /**
     * Returns the time this group has been fetched
     *
     * @return int The timestamp of the last fetch time
     */
    public function getFetchTime() {
        return $this->fetchTime;
    }

    /**
     * Returns the Steam Community friends of this user
     *
     * If the friends haven't been fetched yet, this is done now.
     *
     * @return The friends of this user
     * @see fetchFriends()
     */
    public function getFriends() {
        if(empty($this->friends)) {
            $this->fetchFriends();
        }

        return $this->friends;
    }

    /**
     * Returns the URL of the full-sized version of this user's avatar
     *
     * @return string The URL of the full-sized avatar
     */
    public function getFullAvatarUrl() {
        return $this->imageUrl . '_full.jpg';
    }

    /**
     * Returns the games this user owns
     *
     * The keys of the hash are the games' application IDs and the values are
     * the corresponding game instances.
     *
     * If the friends haven't been fetched yet, this is done now.
     *
     * @return array The games this user owns
     * @see fetchGames()
     */
    public function getGames() {
        if(empty($this->games)) {
            $this->fetchGames();
        }

        return $this->games;
    }

    /**
     * Returns the stats for the given game for the owner of this SteamID
     *
     * @param mixed $id The full or short name or the application ID of the
     *        game stats should be fetched for
     * @return GameStats The statistics for the game with the given name
     * @throws SteamCondenserException if the user does not own this game or it
     *         does not have any stats
     */
    public function getGameStats($id) {
        $game = $this->findGame($id);

        if(!$game->hasStats()) {
            throw new SteamCondenserException("\"{$game->getName()}\" does not have stats.");
        }

        if(empty($this->customUrl)) {
            return GameStats::createGameStats($this->steamId64, $game->getShortName());
        } else {
            return GameStats::createGameStats($this->customUrl, $game->getShortName());
        }
    }

    /**
     * Returns the URL of the icon version of this user's avatar
     *
     * @return The URL of the icon-sized avatar
     */
    public function getIconAvatarUrl() {
        return $this->imageUrl . '.jpg';
    }

    /**
     * Returns the URL of the medium-sized version of this user's avatar
     *
     * @return The URL of the medium-sized avatar
     */
    public function getMediumAvatarUrl() {
        return $this->imageUrl . '_medium.jpg';
    }

    /**
     * Returns the Steam nickname of the user
     *
     * @return The Steam nickname of the user
     */
    public function getNickname() {
        return $this->nickname;
    }

    /**
     * Returns the time in minutes this user has played this game in the last
     * two weeks
     *
     * @param mixed $id The full or short name or the application ID of the
     *        game
     * @return int The number of minutes this user played the given game in the
     *         last two weeks
     */
    public function getRecentPlaytime($id) {
        $game = $this->findGame($id);
        $playtimes = $this->playtimes[$game->getAppId()];

        return $playtimes[0];
    }

    /**
     * Returns the total time in minutes this user has played this game
     *
     * @param mixed $id The full or short name or the application ID of the
     *        game
     * @return int The total number of minutes this user played the given game
     */
    public function getTotalPlaytime($id) {
        $game = $this->findGame($id);
        $playtimes = $this->playtimes[$game->getAppId()];

        return $playtimes[1];
    }

    /**
     * Returns whether the owner of this Steam ID is VAC banned
     *
     * @return <var>true</var> if the user has been banned by VAC
     */
    public function isBanned() {
        return $this->vacBanned;
    }

    /**
     * Returns whether the data for this Steam ID has already been fetched
     *
     * @return <var>true</var> if the Steam ID's data has been
     *         fetched
     */
    public function isFetched() {
        return !empty($this->fetchTime);
    }

    /**
     * Returns whether the owner of this Steam ID is playing a game
     *
     * @return <var>true</var> if the user is in-game
     */
    public function isInGame() {
        return $this->onlineState == 'in-game';
    }

    /**
     * Returns whether the owner of this Steam ID is currently logged into
     * Steam
     *
     * @return <var>true</var> if the user is online
     */
    public function isOnline() {
        return ($this->onlineState == 'online') || ($this->onlineState == 'in-game');
    }
}
?>
