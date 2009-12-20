<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage SteamPacket
 */

require_once "steam/packets/SteamPacket.php";

/**
 * @package Steam Condenser (PHP)
 * @subpackage SteamPacket
 */
class S2A_RULES_Packet extends SteamPacket
{
	/**
	 * @var String[]
	 */
	private $rulesArray;

	/**
	 * @param String $contentData
	 */
	public function __construct($contentData)
	{
		if(empty($contentData))
		{
			throw new Exception("Wrong formatted S2A_RULES packet.");
		}
		parent::__construct(SteamPacket::S2A_RULES_HEADER);

		$contentData = unpack("vrulesNumber/a*rulesData", $contentData);
		$tmpRulesArray = explode("\0", $contentData["rulesData"]);

		// This is a workaround for servers sending corrupt replies
		if(sizeof($tmpRulesArray) % 2) {
			array_pop($tmpRulesArray);
		}

		for($x = 0; $x < sizeof($tmpRulesArray); $x++)
		{
			$this->rulesArray[$tmpRulesArray[$x]] = $tmpRulesArray[++$x];
		}
	}

	/**
	 * @return String[]
	 */
	public function getRulesArray()
	{
		return $this->rulesArray;
	}
}
?>
