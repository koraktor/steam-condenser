<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 */

/**
 * This class represents a game available on Steam
 *
 * @author     Sebastian Staudt
 * @package    steam-condenser
 * @subpackage community
 */
class SteamGame {

    /**
     * @var array
     */
    private static $games = array();

    /**
     * @var int
     */
    private $appId;

    /**
     * @var string
     */
    private $name;

    /**
     * @var string
     */
    private $shortName;

    /**
     * Creates a new or cached instance of the game specified by the given XML
     * data
     *
     * @param SimpleXMLElement $gameData The XML data of the game
     * @return SteamGame The game instance for the given data
     * @see __construct()
     */
    public static function create($gameData) {
        $appId = (int) $gameData->appID;

        if(array_key_exists($appId, self::$games)) {
            return self::$games[$appId];
        } else {
            return new SteamGame($appId, $gameData);
        }
    }

    /**
     * Creates a new instance of a game with the given data and caches it
     *
     * @param int $appId The application ID of the game
     * @param SimpleXMLElement $gameData The XML data of the game
     */
    private function __construct($appId, $gameData) {
        $this->appId = $appId;
        $this->name  = (string) $gameData->name;
        if($gameData->globalStatsLink != null && !empty($gameData->globalStatsLink)) {
            preg_match('#http://steamcommunity.com/stats/([^?/]+)/achievements/#', (string) $gameData->globalStatsLink, $shortName);
            $this->shortName = strtolower($shortName[1]);
        } else {
            $this->shortName = null;
        }

        self::$games[$appId] = $this;
    }

    /**
     * Returns the Steam application ID of this game
     *
     * @return int The Steam application ID of this game
     */
    public function getAppId() {
        return $this->appId;
    }

    /**
     * Returns the full name of this game
     *
     * @return string The full name of this game
     */
    public function getName() {
        return $this->name;
    }

    /**
     * Returns the short name of this game (also known as "friendly name")
     *
     * @return string The short name of this game
     */
    public function getShortName() {
        return $this->shortName;
    }

    /**
     * Returns whether this game has statistics available
     *
     * @return bool <var>true</var> if this game has stats
     */
    public function hasStats() {
        return $this->shortName != null;
    }

}
?>
