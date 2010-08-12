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
 * @subpackage RCONExecResponse
 */

require_once STEAM_CONDENSER_PATH . 'steam/packets/rcon/RCONPacket.php';

class RCONExecResponse extends RCONPacket
{
	public function __construct($requestId, $commandResponse)
	{
		parent::__construct($requestId, RCONPacket::SERVERDATA_RESPONSE_VALUE, $commandResponse);
	}

	public function getResponse()
	{
		$response = $this->contentData->_array();
		return substr($response, 0, strlen($response) - 2);
	}
}
?>
