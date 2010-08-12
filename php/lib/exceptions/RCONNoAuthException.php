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
 * @subpackage Exceptions
 */

require_once STEAM_CONDENSER_PATH . 'exceptions/SteamCondenserException.php';

/**
 * @package Steam Condenser (PHP)
 * @subpackage Exceptions
 */
class RCONNoAuthException extends SteamCondenserException
{
	public function __construct()
	{
		parent::__construct("Not authenticated yet.");
	}
}
?>
