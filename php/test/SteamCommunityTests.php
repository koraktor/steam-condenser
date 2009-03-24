<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 *
 * @author  Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @version $Id$
 */

ini_set("include_path", ini_get("include_path") . PATH_SEPARATOR . dirname(__FILE__) . "/../lib");
error_reporting(E_ALL & ~E_USER_NOTICE);

require_once "steam/community/SteamId.php";

require_once "PHPUnit/Framework.php";

/**
 * @package    Steam Condenser (PHP)
 * @subpackage Tests
 */
class SteamCommunityTests extends PHPUnit_Framework_TestCase
{
	public function testSteamIdByCustomUrl()
	{
		$steamId = new SteamId("Koraktor");
		print_r($steamId);
                print_r($steamId->getGameStats("TF2"));
	}

        public function testSteamIdBySteamId64()
	{
		$steamId = new SteamId(76561197961384956);
		print_r($steamId);
                print_r($steamId->getGameStats("TF2"));
	}
}
?>