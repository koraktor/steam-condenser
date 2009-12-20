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
class A2S_SERVERQUERY_GETCHALLENGE_Packet extends SteamPacket
{
	/**
	 *
	 */
	public function __construct()
	{
		parent::__construct(SteamPacket::A2S_SERVERQUERY_GETCHALLENGE_HEADER);
	}
}
?>