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

require_once STEAM_CONDENSER_PATH . 'ByteBuffer.php';
require_once STEAM_CONDENSER_PATH . 'UDPSocket.php';
require_once STEAM_CONDENSER_PATH . 'exceptions/TimeoutException.php';
require_once STEAM_CONDENSER_PATH . 'steam/packets/SteamPacketFactory.php';

/**
 * @package    Steam Condenser (PHP)
 * @subpackage Sockets
 */
abstract class SteamSocket
{
    /**
     * @var int
     */
    private static $timeout = 1000;

	/**
	 * @var ByteBuffer
	 */
	protected $buffer;

    /**
     * @var UDPSocket
     */
    protected $socket;

    /**
     * Sets the timeout for socket operations. This usually only affects
     * timeouts, i.e. when a server does not respond in time.
     *
     * Due to the server-side implementation of the RCON protocol, each RCON
     * request will also wait this amount of time after execution. So if you
     * need RCON requests to execute fast, you should set this to a adequatly
     * low value.
     *
     * @param $timeout The amount of milliseconds before a request times out
     */
    public static function setTimeout($timeout) {
        self::$timeout = $timeout;
    }

    public function __construct($ipAddress, $portNumber = 27015) {
        $this->socket = new UDPSocket();
        $this->socket->connect($ipAddress, $portNumber);
    }

    /**
     * Closes this socket
     *
     * @see #close
     */
    public function __destruct() {
        $this->close();
    }

    /**
     * Closes the underlying UDPSocket
     *
     * @see UDPSocket#close
     */
    public function close() {
        $this->socket->close();
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
        if(!$this->socket->select(self::$timeout)) {
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

        $data = $this->socket->recv($this->buffer->remaining());
        $this->buffer->put($data);
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

        $this->socket->send($dataPacket->__toString());
	}
}
?>
