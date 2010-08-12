<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 *
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage SocketChannel
 */

require_once STEAM_CONDENSER_PATH . 'ByteBuffer.php';
require_once STEAM_CONDENSER_PATH . 'TCPSocket.php';

class SocketChannel
{
	/**
	 * @var bool
	 */
	private $connected;

	/**
	 * @var TCPSocket
	 */
	private $socket;

	public static function open()
	{
		return new SocketChannel();
	}

	public function __construct()
	{
		$this->socket = new TCPSocket();
		$this->connected = false;
	}

	public function connect(InetAddress $ipAddress, $portNumber)
	{
		$this->socket->connect($ipAddress, $portNumber);
		$this->connected = true;

		return $this;
	}

	public function isConnected()
	{
		return $this->connected;
	}

	public function read(ByteBuffer $destinationBuffer)
	{
		$length = $destinationBuffer->remaining();
		$data = $this->socket->recv($length);
		$destinationBuffer->put($data);

		return strlen($data);
	}

	public function socket()
	{
		return $this->socket;
	}

	public function write(ByteBuffer $sourceBuffer)
	{
		return $this->socket->send($sourceBuffer->get());
	}
}
?>
