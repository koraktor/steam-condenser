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

require_once STEAM_CONDENSER_PATH . 'steam/community/GameLeaderboard.php';

/**
 * The GameLeaderboardManager class represents the game leaderboards for a specific game
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class GameLeaderboardManager {
	
	/**
     * @var array
     */
    protected $leaderboards;
	
	/**
     * @var int
     */
	protected $leaderboardCount;

    /**
     * Creates a GameLeaderboards object and fetchs data for all given game's leaderboards
     * from Steam Community
     * @param $gameName
     */
    public function __construct($gameName) {
        $url = "http://steamcommunity.com/stats/{$gameName}/leaderboards/?xml=1";

        $xml = new SimpleXMLElement(file_get_contents($url));

        if($xml->error != null && !empty($xml->error)) {
            throw new SteamCondenserException((string) $xml->error);
        }

		$this->leaderboardCount = (int) $xml->leaderboardCount;
		
		$this->leaderboards = array();
		foreach ($xml->leaderboard as $boardData) {
			$this->leaderboards[] = new GameLeaderboard($boardData);
		}
    }
	
	/**
     * Returns the GameLeaderboard matching the given name.
     *
     * @return GameLeaderboard or FALSE if no match
     */
	public function getLeaderboardByName($name) {
		
		foreach ($this->leaderboards as $board) {
			if ($name == $board->getName())
				return $board;
		}
		
		return FALSE;
	}
	
	/**
     * Returns the GameLeaderboard matching the given Leaderboard Id.
     *
     * @return GameLeaderboard or FALSE if no match
     */
	public function getLeaderboardById($id) {
		
		foreach ($this->leaderboards as $board) {
			if ($id == $board->getId())
				return $board;
		}
		
		return FALSE;
	}
	
	/**
     * Returns an array containing all of the game's leaderboards
     */
	public function getLeaderboards() {
		return $this->leaderboards;
	}
	
	/**
     * Returns the count of all of the game's leaderboards
     */
	public function getLeaderboardCount() {
		return $this->leaderboardCount;
	}
}
?>
