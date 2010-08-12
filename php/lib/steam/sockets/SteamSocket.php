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
 * @subpackage Sockets
 */

require_once STEAM_CONDENSER_PATH . 'ByteBuffer.php';
require_once STEAM_CONDENSER_PATH . 'DatagramChannel.php';
require_once STEAM_CONDENSER_PATH . 'InetAddress.php';
require_once STEAM_CONDENSER_PATH . 'exceptions/TimeoutException.php';
require_once STEAM_CONDENSER_PATH . 'steam/packets/SteamPacketFactory.php';

/**
 * @package    Steam Condenser (PHP)
 * @subpackage Sockets
 */
abstract class SteamSocket
{
	/**
	 * @var ByteBuffer
	 */
	protected $buffer;

	/**
	 * @var DatagramChannel
	 */
	protected $channel;

	public function __construct(InetAddress $ipAddress, $portNumber = 27015)
	{
		$this->channel = DatagramChannel::open();
		$this->channel->connect($ipAddress, $portNumber);
		$this->channel->configureBlocking(false);
	}

	public function __destruct()
	{
		//$this->channel->close();
	}
	
	/**
	 * Abstract getReplyData() method
	 * @return byte[]
	 */
	abstract public function getReply();

	/**
	 * @return int
	 */
	public function receivePacket($bufferLength = 0)
	{
        if(!$this->channel->socket()->select(1))
        {
			throw new TimeoutException();
		}

		if($bufferLength == 0)
		{
			$this->buffer->clear();
		}
		else
		{
			$this->buffer = ByteBuffer::allocate($bufferLength);
		}
		 
		$this->channel->read($this->buffer);
		$bytesRead = $this->buffer->position();
		$this->buffer->rewind();
		$this->buffer->limit($bytesRead);
		 
		return $bytesRead;
	}

	/**
	 *
	 */
	public function send(SteamPacket $dataPacket)
	{
		trigger_error("Sending packet of type \"" . get_class($dataPacket) . "\"...");

		$this->buffer = ByteBuffer::wrap($dataPacket->__toString());
		$this->channel->write($this->buffer);
	}
}
?>
