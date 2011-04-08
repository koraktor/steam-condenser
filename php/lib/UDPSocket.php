<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 *
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage UDPSocket
 */

require_once STEAM_CONDENSER_PATH . 'Socket.php';

/**
 * This class represents a UDP socket
 *
 * It can connect to a remote host, send and receive packets
 *
 * @package Steam Condenser (PHP)
 * @subpackage UDPSocket
 */
class UDPSocket extends Socket
{
	/**
	 * Connects the UDP socket to the host with the given IP address and port number
	 */
	public function connect($ipAddress, $portNumber)
	{
		$this->ipAddress = $ipAddress;
		$this->portNumber = $portNumber;

		if($this->socketsEnabled)
		{
			if(!$this->socket = socket_create(AF_INET, SOCK_DGRAM, SOL_UDP))
			{
				$errorCode = socket_last_error($this->socket);
				throw new Exception("Could not create socket: " . socket_strerror($errorCode));
			}
			if(@!socket_connect($this->socket, $ipAddress, $portNumber))
			{
				$errorCode = socket_last_error($this->socket);
				throw new Exception("Could not connect socket: " . socket_strerror($errorCode));
			}
		}
		else
		{
			if(!$this->socket = fsockopen("udp://$ipAddress", $portNumber, $socketErrno, $socketErrstr, 2))
			{
				throw new Exception("Could not create socket: $socketErrstr");
			}
			stream_set_blocking($this->socket, true);
		}
	}
}
?>
