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
 * @subpackage Socket
 */

abstract class Socket
{
	/**
	 * @var boolean
	 */
	protected $isBlocking = true;

	/**
	 * The IP address the socket is connected to
	 * @var InetAddress
	 */
	protected $ipAddress;

	/**
	 * The port number the socket is connected to
	 * @var int
	 */
	protected $portNumber;

	/**
	 * @var String
	 */
	protected $readBuffer = "";

	/**
	 * The socket itself
	 * @var resource
	 */
	protected $socket;

	/**
	 * Stores if the sockets extension is loaded
	 * @var bool
	 */
	protected $socketsEnabled;

	/**
	 * Constructs the Socket object
	 */
	public function __construct()
	{
		$this->socketsEnabled = extension_loaded("sockets");
	}

	public function __desctruct()
	{
		$this->close();
	}

	abstract public function connect(InetAddress $ipAddress, $portNumber);

	/**
	 * Closes the socket
	 */
	public function close()
	{
		if($this->socketsEnabled)
		{
			socket_close($this->socket);
		}
		else
		{
			fclose($this->socket);
		}
	}

	/**
	 * @return byte
	 */
	public function getByte()
	{
		return ord($this->read(1));
	}

	/**
	 * @return float
	 */
	public function getFloat()
	{
		return floatval($this->read(4));
	}

	/**
	 * @return long
	 */
	public function getLong()
	{
		$reply = unpack("Vlong", $this->read(4));
		return $reply["long"];
	}

	/**
	 * @return String
	 */
	public function flushBuffer()
	{
		$reply = $this->readBuffer;
		$this->readBuffer = "";
		return $reply;
	}

	/**
	 * @return short
	 */
	public function getShort()
	{
		$reply = unpack("vshort", $this->read(2));
		return $reply["short"];
	}

	/**
	 * @return String
	 */
	public function getString()
	{
		$returnString = null;
		while(true)
		{
			$byte = $this->getByte();
			if($byte == 0)
			{
				break;
			}
			$returnString .= chr($byte);
		}

		return $returnString;
	}

	/**
	 * @param int $length
	 * @return String
	 */
	public function recv($length = 128)
	{
		if($this->socketsEnabled)
		{
			$data = @socket_read($this->socket, $length, PHP_BINARY_READ);
		}
		else
		{
			$data = fread($this->socket, $length);
		}

		if(!$data)
		{
			throw new Exception("Could not read from socket.");
		}

		return $data;
	}

	/**
	 * @return boolean
	 */
	public function select($timeout = 0)
	{
		$read = array($this->socket);
		$write = null;
		$except = null;

		if($this->socketsEnabled)
		{
			$select = socket_select($read, $write, $except, $timeout);
		}
		else
		{
			$select = stream_select($read, $write, $except, $timeout);
		}

		return $select > 0;
	}

	/**
	 * @param String $data
	 */
	public function send($data)
	{
		if($this->socketsEnabled)
		{
			$sendResult = socket_send($this->socket, $data, strlen($data), 0);
		}
		else
		{
			$sendResult = fwrite($this->socket, $data, strlen($data));
		}

		if(!$sendResult)
		{
			throw new Exception("Could not send data.");
		}
	}

	/**
	 *
	 */
	public function setBlock($doBlock)
	{
		$this->isBlocking = $doBlock;
	}

	/**
	 *
	 */
	public function socket()
	{
		return $this->socket;
	}
}
?>
