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
 * The TF2Stats class represents the game statistics for a single user in Team
 * Fortress 2
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class TF2Stats extends GameStats
{
	/**
	 * Creates a TF2Stats object by calling the super constructor with the game
	 * name "TF2"
	 * @var $steamId
	 */
	public function __construct($steamId)
	{
		parent::__construct($steamId, "TF2");
	}

	/**
	 * Returns a Hash of TF2Class for this user containing all Team Fortress 2
	 * classes. If the classes haven't been parsed already, parsing is done now.
	 */
	public function getClassStats()
	{
		if(empty($this->classStats))
		{
			foreach($this->xmlData->stats->classData as $classData)
			{
				$this->classStats["className"] = new TF2Class($classData);
			}
		}

		return $this->classStats;
	}
}
?>
