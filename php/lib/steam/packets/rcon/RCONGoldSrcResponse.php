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
 * @subpackage RCON packets
 */

require_once STEAM_CONDENSER_PATH . 'steam/packets/rcon/RCONPacket.php';

/**
 * @package Steam Condenser (PHP)
 * @subpackage RCON packets
 */
class RCONGoldSrcResponse extends SteamPacket
{
	public function __construct($commandResponse)
	{
		parent::__construct(SteamPacket::RCON_GOLDSRC_RESPONSE_HEADER, $commandResponse);
	}

	public function getResponse()
	{
		return substr($this->contentData->_array(), 0, -2);
	}
}
?>
