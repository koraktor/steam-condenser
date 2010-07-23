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

/**
 * The GameAchievement class represents a specific achievement for a single game
 * and for a single user
 *
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class GameAchievement {

    /**
     * @var int
     */
    private $appId;

    /**
     * @var String
     */
    private $name;

    /**
     * @var String
     */
    private $steamId64;

    /**
     * @var int
     */
    private $timestamp;

    /**
     * @var bool
     */
    private $unlocked;

    /**
     * Creates the achievement with the given name for the given user and game
     * and achievement data
     *
     * @param String steamId64 The 64bit SteamID of the player
     * @param int appId The AppID of the game this achievement belongs to
     * @param SimpleXMLElement achievementData The XML data for this achievement
     */
    public function __construct($steamId64, $appId, $achievementData) {
        $this->appId     = $appId;
        $this->name      = (string) $achievementData->name;
        $this->steamId64 = $steamId64;
        $this->unlocked  = (bool) $achievementData->closed;

        if($this->unlocked && $achievementData->unlockTimestamp != null) {
            $this->timestamp = (int) $achievementData->unlockTimestamp;
        }
    }

    /**
     * Returns the AppID of the game this achievements belongs to
     *
     * @return int
     */
    public function getAppId() {
        return $this->appId;
    }

    /**
     * Returns the name of this achievement
     *
     * @return String
     */
    public function getName() {
        return $this->name;
    }

    /**
     * Returns the 64bit SteamID of the player this achievement belongs to
     *
     * @return String
     */
    public function getSteamId64() {
        return $this->steamId64;
    }

    /**
     * Returns the timestamp at which this achievement has been unlocked
     *
     * @return int
     */
    public function getTimestamp() {
        return $this->timestamp;
    }

    /**
     * Returns whether this achievement has been unlocked by its owner
     *
     * @return boolean
     */
    public function isUnlocked() {
        return $this->unlocked;
    }
}
?>
