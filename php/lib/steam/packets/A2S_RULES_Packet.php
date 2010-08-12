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

require_once STEAM_CONDENSER_PATH . 'steam/packets/RequestPacketWithChallenge.php';

/**
 * @package Steam Condenser (PHP)
 * @subpackage Packets
 */
class A2S_RULES_Packet extends RequestPacketWithChallenge
{
	/**
	 * @param long $challengeNumber
	 */
	public function __construct($challengeNumber = "\xFF\xFF\xFF\xFF")
	{
		parent::__construct(SteamPacket::A2S_RULES_HEADER, $challengeNumber);
	}
}
?>