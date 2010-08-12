<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage SteamPacket
 */

require_once STEAM_CONDENSER_PATH . 'steam/packets/SteamPacket.php';

/**
 * @package Steam Condenser (PHP)
 * @subpackage SteamPacket
 */
abstract class RequestPacketWithChallenge extends SteamPacket
{
	/**
	 * Returns a packed version of the packet data
	 * @return String
	 */
	public function __toString()
	{
		return pack("cccccV", 0xFF, 0xFF, 0xFF, 0xFF, $this->headerData, $this->contentData->_array());
	}
}
?>