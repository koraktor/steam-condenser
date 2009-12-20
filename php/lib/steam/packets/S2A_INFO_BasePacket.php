<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage Packets
 */

require_once "steam/packets/SteamPacket.php";

/**
 * @package Steam Condenser (PHP)
 * @subpackage Packets
 */
abstract class S2A_INFO_BasePacket extends SteamPacket
{
	/**
	 * @var String
	 */
	private $mapName;

	/**
	 * @var int
	 */
	private $networkVersion;

	/**
	 * @var String
	 */
	private $serverName;

	/**
	 * @return mixed[]
	 */
	public function getInfoHash()
	{
		return array_diff_key(get_object_vars($this), array("contentData" => null, "headerData" => null));
	}
}
?>
