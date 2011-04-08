<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage Sockets
 */

require_once STEAM_CONDENSER_PATH . 'exceptions/PacketFormatException.php';
require_once STEAM_CONDENSER_PATH . 'steam/sockets/SteamSocket.php';

/**
 * @package Steam Condenser (PHP)
 * @subpackage Sockets
 */
class MasterServerSocket extends SteamSocket
{

	/**
	 * @throws PacketFormatException
	 */
	public function getReply()
	{
		$this->receivePacket(1500);

		if($this->buffer->getLong() != -1)
		{
			throw new PacketFormatException("Master query response has wrong packet header.");
		}

		return SteamPacketFactory::getPacketFromData($this->buffer->get());
	}

}
?>
