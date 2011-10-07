<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Nicholas Hastings
 *
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 */

/**
 * The GameLeaderboard class represents a leaderboard entry in a specific leaderboard
 *
 * @author     Nicholas Hastings
 * @package    steam-condenser
 * @subpackage community
 */
class GameLeaderboardEntry {

    /**
     * @var string
     */
    protected $steamId64;

    /**
     * @var int
     */
    protected $score;

    /**
     * @var int
     */
    protected $rank;

    /**
     * @var GameLeaderboard
     */
    protected $leaderboard;

    /**
     * Creates a GameLeaderboardEntry object
     * @param SimpleXMLElement $entryData The entry data from Steam Community
     * @param GameLeaderboard $leaderboard The parent leaderboard that this entry belongs to
     */
    public function __construct($entryData, $leaderboard) {
        $this->steamId64   = SteamId::create((string) $entryData->steamid, false);
        $this->score       = (int)    $entryData->score;
        $this->rank        = (int)    $entryData->rank;
        $this->leaderboard = $leaderboard;
    }

    /**
     * @return SteamId
     */
    public function getSteamId64() {
        return $this->steamId64;
    }

    /**
     * @return int
     */
    public function getScore() {
        return $this->score;
    }

    /**
     * @return int
     */
    public function getRank() {
        return $this->rank;
    }

    /**
     * @return GameLeaderboard
     */
    public function getLeaderboard() {
        return $this->leaderboard;
    }
}
?>
