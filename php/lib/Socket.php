<?php
/**
 * @author Sebastian Staudt
 * @package Steam Interface Package (PHP)
 * @subpackage Socket
 * @version $Id$
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
	 * @var String
	 */
	protected $readBuffer = "";
	
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
			if(!$this->socket = socket_create(AF_INET, SOCK_DGRAM, SOL_UDP))
			{
				$errorCode = socket_last_error($this->socket);
				throw new Exception("Could not create socket: " . socket_strerror($errorCode));
			}
		}
		else
		{
			if(!$this->socket = fsockopen("udp://$ipAddress", $portNumber, $socketErrno, $socketErrstr, 2))
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
	 * @param int $length
	 * @return String
	 */
	protected function read($length = 128)
	{
		if(empty($this->readBuffer))
		{
			throw new Exception("No data to read.");
		}
		
		$replyData = substr($this->readBuffer, 0, $length);
		$this->readBuffer = substr($this->readBuffer, $length);
		
		return $replyData;
	}
	
	/**
	 */
	protected function readToBuffer($length = 128)
	{
		$read = array($this->socket);
		$write = null;
		$except = null;
		
		if($this->socketsEnabled)
		{
			if(socket_select($read, $write, $except, 1))
			{
				$replyData = socket_read($this->socket, $length, PHP_BINARY_READ);
			}
		}
		elseif(stream_select($read, $write, $except, 1))
		{
			$replyData = fread($this->socket, $length);
		}
		else
		{
			throw new Exception("No data received.");
		}
		
		debug("Received data: " . bin2hex($replyData));
		
		$this->readBuffer .= $replyData;
	}
	
	/**
	 * @param String $data
	 */
	public function send($data)
	{
		debug("Sending data: " . bin2hex($data));
	
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