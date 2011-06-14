<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage Steam Community
 */

require_once STEAM_CONDENSER_PATH . 'steam/community/GameLeaderboardEntry.php';
require_once STEAM_CONDENSER_PATH . 'steam/community/SteamId.php';

/**
 * The GameLeaderboard class represents a game leaderboard for a specific game
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class GameLeaderboard
{
	// From OpenSteamworks
	const LeaderboardDisplayTypeNone             = 0;
	const LeaderboardDisplayTypeNumeric          = 1;
	const LeaderboardDisplayTypeTimeSeconds      = 2;
	const LeaderboardDisplayTypeTimeMilliSeconds = 3;
	
	const LeaderboardSortMethodNone              = 0;
	const LeaderboardSortMethodAscending         = 1;	// top-score is lowest number
	const LeaderboardSortMethodDescending        = 2;	// top-score is highest number
	//
	
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
     * Creates a GameLeaderboard object
     * @param $gameName
     */
	public function __construct($boardData) {
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
		$id = '';
		if (is_object($steamId)) {
			$id = $steamId->getSteamId64();
		}
		else {
			$id = $steamId;
		}
		
		$fullurl = sprintf('%s&steamid=%s', $this->url, $id);
		$xml = new SimpleXMLElement(file_get_contents($fullurl));
		
		if($xml->error != null && !empty($xml->error)) {
            throw new SteamCondenserException((string) $xml->error);
        }
		
		foreach ($xml->entries->entry as $entryData) {
			if ($entryData->steamid != $id)
				continue;
			
			return new GameLeaderboardEntry($entryData, $this);
		}
		
		return FALSE;
	}
	
	/**
     * Returns an array of GameLeaderboardEntrys on this GameLeaderboard for given steamId
	 * and friends of given steamId
	 * @param steam64 string OR fetched SteamId object
	 * @return array
	 */
	public function getEntryForSteamIdFriends($steamId) {
		// We'll let users pass SteamId class or steamid64 string
		$id = '';
		if (is_object($steamId)) {
			$id = $steamId->getSteamId64();
		}
		else if (is_numeric($steamId)) {
			$id = $steamId;
		}
		else {
			throw new SteamCondenserException("Invalid steam id. You must pass a steamId64 or a fetched SteamId object.");
		}		
		
		$fullurl = sprintf('%s&steamid=%s', $this->url, $id);
		$xml = new SimpleXMLElement(file_get_contents($fullurl));
		
		if($xml->error != null && !empty($xml->error)) {
            throw new SteamCondenserException((string) $xml->error);
        }
		
		$entries = array();
		foreach ($xml->entries->entry as $entryData) {
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
		if ($last < $first)
			throw new SteamCondenserException('First entry must be prior to last entry for leaderboard entry lookup.');
		if (($last - $first) > 5000)
			throw new SteamCondenserException('Leaderboard entry lookup is currently limited to a maximum of 5001 entries per request.');
		if ($first < 0)
			throw new SteamCondenserException(sprintf('Leaderboards start at entry 1 (%d)', $first));
		if ($last > $this->entryCount)
			throw new SteamCondenserException(sprintf('Last entry is out of bounds. (%d/%d)', $last, $this->entryCount));
		
		$fullurl = sprintf('%s&start=%d&end=%d', $this->url, $first, $last);
		$xml = new SimpleXMLElement(file_get_contents($fullurl));
		
		if($xml->error != null && !empty($xml->error)) {
            throw new SteamCondenserException((string) $xml->error);
        }
		
		$entries = array();
		foreach ($xml->entries->entry as $entryData) {
			$rank = (int) $entryData->rank;
			$entries[$rank] = new GameLeaderboardEntry($entryData, $this);
		}
		
		return $entries;
	}
}
?>
