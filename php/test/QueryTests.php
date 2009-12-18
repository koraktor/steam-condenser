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
 */

ini_set("include_path", ini_get("include_path") . PATH_SEPARATOR . dirname(__FILE__) . "/../lib");
error_reporting(E_ALL & ~E_USER_NOTICE);

require_once "InetAddress.php";
require_once "steam/servers/GoldSrcServer.php";
require_once "steam/servers/MasterServer.php";
require_once "steam/servers/SourceServer.php";

require_once "PHPUnit/Framework.php";

/**
 * @package    Steam Condenser (PHP)
 * @subpackage Tests
 */
class QueryTests extends PHPUnit_Framework_TestCase
{
	/**
	 * @expectedException TimeoutException
	 */
	public function testInvalidGoldSrcServer()
	{
		$server = new GoldSrcServer(new InetAddress("1.0.0.0"), 27015);
		$server->getPing();
	}

	/**
	 * @expectedException TimeoutException
	 */
	public function testInvalidSourceServer()
	{
		$server = new SourceServer(new InetAddress("1.0.0.0"), 27015);
		$server->getPing();
	}

	public function testRandomGoldsrcServer()
	{
		$masterServer = new MasterServer(MasterServer::GOLDSRC_MASTER_SERVER);
		$serverArray = $masterServer->getServers(MasterServer::REGION_ALL, "\\type\\d\\empty\\1\\full\\1\\gamedir\\valve");
		$serverAddress = $serverArray[array_rand($serverArray)];
		$server = new GoldSrcServer(new InetAddress($serverAddress[0]), $serverAddress[1]);
		$server->initialize();
		$server->updatePlayerInfo();
		$server->updateRulesInfo();

		$this->assertNotNull($server->getPing());
		$this->assertNotNull($server->getPlayers());
		$this->assertNotNull($server->getRules());
		$this->assertNotNull($server->getServerInfo());

		echo $server;
	}

	public function testRandomSourceServer()
	{
		$masterServer = new MasterServer(MasterServer::SOURCE_MASTER_SERVER);
		$serverArray = $masterServer->getServers(MasterServer::REGION_ALL, "\\type\\d\\empty\\1\\full\\1\\gamedir\\tf");
		$serverAddress = $serverArray[array_rand($serverArray)];
		$server = new SourceServer(new InetAddress($serverAddress[0]), $serverAddress[1]);
		$server->initialize();
		$server->updatePlayerInfo();
		$server->updateRulesInfo();

		$this->assertNotNull($server->getPing());
		$this->assertNotNull($server->getPlayers());
		$this->assertNotNull($server->getRules());
		$this->assertNotNull($server->getServerInfo());

		echo $server;
	}
}
?>
