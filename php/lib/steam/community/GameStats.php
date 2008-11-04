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

/**
 * The GameStats class represents the game statistics for a single user and a
 * specific game
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class GameStats
{
	/**
	 * Used to cache the XML data of the statistics for this game and this user 
	 * @var SimpleXML
	 */
	private $xmlData;
	
	/**
	 * Creates a GameStats object and fetchs data from Steam Community for the
     * given user and game
	 * @param $steamId
	 * @param $gameName
	 */
	public function __construct($steamId, $gameName)
	{
		$this->steamId = $steamId;
		
		$this->xmlData = new SimpleXMLElement("http://www.steamcommunity.com/id/{$this->steamId}/stats/{$this->gameName}?xml=1", null, true);
		
		$this->privacyState = $this->xmlData->privacyState;
		if($this->privacyState == "public")
		{
			$this->accumulatedPoints = intval($this->xmlData->stats->accumulatedPoints);
			$this->appId = intval($this->xmlData);
			$this->gameFriendlyName = $this->gameFriendlyName;
			$this->gameName = $this->gameName;
			$this->hoursPlayed = floatval($this->hoursPlayed);
		}
	}
	
	/**
	 * Returns the achievements for this stats' user and game. If the achievements
     * haven't been parsed already, parsing is done now.
	 * @return GameAchievements[]
	 */
	public function getAchievements()
	{
		if(empty($this->achievements))
		{
			foreach($this->xmlData->achievements as $achievement)
			{
				$this->achievements[] = new GameAchievement($this->steamId, $this->appId, $achievement->name, ($achievement->closed == 1));
			}
		}
		
		return $this->achievements;
	}
}
?>
