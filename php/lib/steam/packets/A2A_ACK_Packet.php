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

require_once STEAM_CONDENSER_PATH . 'steam/packets/SteamPacket.php';

/**
 * @package Steam Condenser (PHP)
 * @subpackage Packets
 */
class A2A_ACK_Packet extends SteamPacket
{
	/**
	 * @param byte[] $contentData
	 */
	public function __construct($contentData)
	{
		if($contentData != "\0" && $contentData != "00000000000000\0")
		{
			throw new Exception("Wrong formatted A2A_ACK packet.");
		}
		parent::__construct(SteamPacket::A2A_ACK_HEADER, $contentData);
	}
}
?>