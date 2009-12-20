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
 * @subpackage DatagramChannel
 */

require_once "ByteBuffer.php";
require_once "InetAddress.php";
require_once "UDPSocket.php";

/**
 * @package Steam Condenser (PHP)
 * @subpackage DatagramChannel
 */
class DatagramChannel
{
	/**
	 * @var Socket
	 */
	private $socket;

	protected function __construct()
	{
		$this->socket = new UDPSocket();
		$this->configureBlocking(true);
	}

	public static function open()
	{
		return new DatagramChannel();
	}

	public function close()
	{
		$this->socket->close();
	}

	public function connect(InetAddress $ipAddress, $portNumber)
	{
		$this->socket->connect($ipAddress, $portNumber);
	}

	public function configureBlocking($doBlock)
	{
		$this->socket->setBlock($doBlock);
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