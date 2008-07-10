<?php
/**
 * @author Sebastian Staudt
 * @package Steam Interface Package (PHP)
 * @subpackage Socket
 * @version $Id: Socket.php 21 2008-02-29 10:39:13Z koraktor $
 */

/**
 * This class represents a UDP-Socket
 * 
 * It can connect to a remote host, send and receive packets
 * 
 * @package Steam Interface Package (PHP)
 * @subpackage Socket
 */
class Socket
{
	/**
	 * The IP address the socket is connected to
	 * @var InetAddress
	 */
	private $ipAddress;
	
	/**
	 * The port number the socket is connected to
	 * @var int
	 */
	private $portNumber;
	
	/**
	 * The socket itself
	 * @var resource
	 */
	private $socket;
	
	/**
	 * Stores if the sockets extension is loaded
	 * @var bool
	 */
	private $socketsEnabled = false;
	
	/**
	 * Opens a UDP socket to the specified IP address and port
	 * @param InetAddress $ipAddress
	 * @param int $portNumber
	 */
	public function __construct(InetAddress $ipAddress, $portNumber)
	{
		if(extension_loaded("sockets"))
		{
			$this->socketsEnabled = true;
			$this->socket = socket_create(AF_INET, SOCK_DGRAM, SOL_UDP);
			if(!socket_bind($this->socket, "0.0.0.0"))
			{
				$errorCode = socket_last_error($this->socket);
				throw new Exception("Could not create socket: " . socket_strerror($errorCode));
			}
		}
		else
		{
			$this->socket = fsockopen("udp://$ipAddress", $portNumber, $socketErrno, $socketErrstr, 2);
			if(!$this->socket)
			{
				throw new Exception("Could not create socket.");
			}
		}
		
		$this->ipAddress = $ipAddress;
		$this->portNumber = $portNumber;
	}
	
	/**
	 * Closes the socket
	 */
	public function __destruct()
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
	 * @var int $length
	 */
	public function getReply($length = 128)
	{
		if($this->socketsEnabled)
		{
			$replyData = socket_read($this->socket, $length, PHP_BINARY_READ);
		}
		else
		{
			$replyData = fread($this->socket, $length);
		}
		
		if(!$replyData)
		{
			throw new Exception("Could not read data.");
		}
		
		return $replyData;
	}

	/**
	 * @param String $errorMessage
	 * @param int $errorCode
	 * @return String
	 * @throws Exception
	 */
	public static function printError($errorMessage, $errorCode)
	{
		if(!is_int($errorCode))
		{
			throw new Exception("$errorCode is not an integer.");
		}
	
		throw new Exception($errorMessage . ": " . socket_strerror($errorCode) . " ($errorCode)");
	}
	
	/**
	 * @param String $data
	 */
	public function send($data)
	{
		if($this->socketsEnabled)
		{
			$sendResult = socket_sendto($this->socket, $data, strlen($data), 0, $this->ipAddress, $this->portNumber);
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
}
?>