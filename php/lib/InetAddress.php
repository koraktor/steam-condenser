<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 *
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Source Condenser (PHP)
 * @subpackage InetAddress
 */

/**
 * @package Source Condenser (PHP)
 * @subpackage InetAddress
 */
class InetAddress
{
	/**
	 * This array saves the octets of the IP address
	 * @var int[4]
	 */
	private $inetAddress = "127.0.0.1";

	/**
	 * @param String $inetAddress
	 * @since v0.1
	 */
	public function __construct($inetAddress = null)
	{
		if(!is_string($inetAddress))
		{
			throw new Exception("Parameter has to be a String.");
		}

		$this->inetAddress = gethostbyname($inetAddress);
	}

	/**
	 * @return String
	 */
	public function __toString()
	{
		return $this->inetAddress;
	}
}
?>