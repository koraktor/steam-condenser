<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage Exceptions
 */

require_once 'exceptions/SteamCondenserException.php';

/**
 * @package Steam Condenser (PHP)
 * @subpackage Exceptions
 */
class RCONBanException extends SteamCondenserException
{
	public function __construct()
	{
		parent::__construct('You have been banned from this server.');
	}
}
?>
