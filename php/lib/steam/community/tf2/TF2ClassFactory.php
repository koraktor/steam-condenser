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
 * @subpackage Steam Community
 */

require_once "steam/community/tf2/TF2Class.php";
require_once "steam/community/tf2/TF2Engineer.php";
require_once "steam/community/tf2/TF2Medic.php";
require_once "steam/community/tf2/TF2Sniper.php";
require_once "steam/community/tf2/TF2Spy.php";

/**
 * The TF2ClassFactory is used to created instances of TF2Class based on the
 * XML input data
 * @package    Steam Condenser (PHP)
 * @subpackage Steam Community
 */
abstract class TF2ClassFactory
{
	/**
	 * Creates a new instance of TF2Class storing the statistics for a Team
	 * Fortress 2 class with the assigned XML data
	 * @return TF2Class
	 */
	public static function getTF2Class(SimpleXMLElement $classData)
	{
		switch($classData->className)
		{
			case "Engineer":
				return new TF2Engineer($classData);
				break;
			case "Medic":
				return new TF2Medic($classData);
				break;
			case "Sniper":
				return new TF2Sniper($classData);
				break;
			case "Spy":
				return new TF2Spy($classData);
				break;
			default:
				return new TF2Class($classData);
		}
	}
}

?>
