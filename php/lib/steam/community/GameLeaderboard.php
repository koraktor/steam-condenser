<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Nicholas Hastings
 *
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 */

require_once STEAM_CONDENSER_PATH . 'steam/community/GameLeaderboardEntry.php';
require_once STEAM_CONDENSER_PATH . 'steam/community/SteamId.php';

/**
 * The GameLeaderboard class represents a game leaderboard for a specific game
 *
 * @author     Nicholas Hastings
 * @package    steam-condenser
 * @subpackage community
 */
class GameLeaderboard
{
    // From OpenSteamworks
    const LEADERBOARD_DISPLAY_TYPE_NONE         = 0;
    const LEADERBOARD_DISPLAY_TYPE_NUMERIC      = 1;
    const LEADERBOARD_DISPLAY_TYPE_SECONDS      = 2;
    const LEADERBOARD_DISPLAY_TYPE_MILLISECONDS = 3;

    const LEADERBOARD_SORT_METHOD_NONE = 0;
    const LEADERBOARD_SORT_METHOD_ASC  = 1; // top-score is lowest number
    const LEADERBOARD_SORT_METHOD_DESC = 2; // top-score is highest number
    //

    /**
     * @var array
     */
    private static $leaderboards = array();

    /**
     * @var int
     */
    protected $id;

    /**
     * @var string
     */
    protected $url;

    /**
     * @var string
     */
    protected $name;

    /**
     * @var int
     */
    protected $entryCount;

    /**
     * @var int
     */
    protected $sortMethod;

    /**
     * @var int
     */
    protected $displayType;

    /**
     * Returns the GameLeaderboard matching the given Leaderboard Id.
     *
     * @return GameLeaderboard or FALSE if no match
     */
    public static function getLeaderboard($gameName, $id) {
        $getter = is_string($id) ? 'getName' : 'getId';

        foreach(self::getLeaderboards($gameName) as $board) {
            if($id == $board->$getter()) {
                return $board;
            }
        }
    }

    /**
     * Returns an array containing all of the game's leaderboards
     */
    public static function getLeaderboards($gameName) {
        if(!array_key_exists($gameName, self::$leaderboards)) {
            self::loadGameLeaderboards($gameName);
        }

        return self::$leaderboards[$gameName];
    }

    private static function loadGameLeaderboards($gameName) {
        $url = "http://steamcommunity.com/stats/$gameName/leaderboards/?xml=1";
        $boardsData = new SimpleXMLElement(file_get_contents($url));

        if(!empty($boardsData->error)) {
            throw new SteamCondenserException((string) $boardsData->error);
        }

        self::$leaderboards[$gameName] = array();
        foreach($boardsData->leaderboard as $boardData) {
            self::$leaderboards[$gameName][] = new GameLeaderboard($boardData);
        }
    }

    /**
     * Creates a GameLeaderboard object
     * @param SimpleXMLElement $boardData
     */
    public function __construct(SimpleXMLElement $boardData) {
        $this->url         = (string) $boardData->url;
        $this->id          = (int)    $boardData->lbid;
        $this->name        = (string) $boardData->name;
        $this->entryCount  = (int)    $boardData->entries;
        $this->sortMethod  = (int)    $boardData->sortmethod;
        $this->displayType = (int)    $boardData->displaytype;
    }

    /**
     * @return String
     */
    public function getName() {
        return $this->name;
    }

    /**
     * @return int
     */
    public function getId() {
        return $this->id;
    }

    /**
     * @return int
     */
    public function getEntryCount() {
        return $this->entryCount;
    }

    /**
     * @return int
     */
    public function getSortMethod() {
        return $this->sortMethod;
    }

    /**
     * @return int
     */
    public function getDisplayType() {
        return $this->displayType;
    }

    /**
     * Returns the GameLeaderboardEntry on this GameLeaderboard for given steamId
     * @param steam64 string OR fetched SteamId object
     * @return GameLeaderboardEntry or FALSE if player is not on leaderboard
     */
    public function getEntryForSteamId($steamId) {
        // We'll let users pass SteamId class or steamid64 string
        if(is_object($steamId)) {
            $id = $steamId->getSteamId64();
        } else {
            $id = $steamId;
        }

        $fullurl = sprintf('%s&steamid=%s', $this->url, $id);
        $xml = new SimpleXMLElement(file_get_contents($fullurl));

        if(!empty($xml->error)) {
            throw new SteamCondenserException((string) $xml->error);
        }

        foreach($xml->entries->entry as $entryData) {
            if($entryData->steamid != $id) {
                continue;
            }

            return new GameLeaderboardEntry($entryData, $this);
        }

        return false;
    }

    /**
     * Returns an array of GameLeaderboardEntrys on this GameLeaderboard for given steamId
     * and friends of given steamId
     * @param steam64 string OR fetched SteamId object
     * @return array
     */
    public function getEntryForSteamIdFriends($steamId) {
        // We'll let users pass SteamId class or steamid64 string
        if(is_object($steamId)) {
            $id = $steamId->getSteamId64();
        } else if (is_numeric($steamId)) {
            $id = $steamId;
        } else {
            throw new SteamCondenserException('Invalid steam id. You must pass a steamId64 or a fetched SteamId object.');
        }

        $fullurl = sprintf('%s&steamid=%s', $this->url, $id);
        $xml = new SimpleXMLElement(file_get_contents($fullurl));

        if(!empty($xml->error)) {
            throw new SteamCondenserException((string) $xml->error);
        }

        $entries = array();
        foreach($xml->entries->entry as $entryData) {
            $rank = (int) $entryData->rank;
            $entries[$rank] = new GameLeaderboardEntry($entryData, $this);
        }

        return $entries;
    }

    /**
     * Returns an array of GameLeaderboardEntrys on this GameLeaderboard for given rank range.
     * Range is inclusive. Currently, a maximum of 5001 entries can be returned in a single request.
     * @param int $first
     * @param int $last
     * @return array
     */
    public function getEntryRange($first, $last) {
        if($last < $first) {
            throw new SteamCondenserException('First entry must be prior to last entry for leaderboard entry lookup.');
        }
        if(($last - $first) > 5000) {
            throw new SteamCondenserException('Leaderboard entry lookup is currently limited to a maximum of 5001 entries per request.');
        }

        $fullurl = sprintf('%s&start=%d&end=%d', $this->url, $first, $last);
        $xml = new SimpleXMLElement(file_get_contents($fullurl));

        if(!empty($xml->error)) {
            throw new SteamCondenserException((string) $xml->error);
        }

        $entries = array();
        foreach($xml->entries->entry as $entryData) {
            $rank = (int) $entryData->rank;
            $entries[$rank] = new GameLeaderboardEntry($entryData, $this);
        }

        return $entries;
    }
}
?>
