<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage Socket
 * @version $Id$
 */

require_once "InetAddress.php";

/**
 * This class represents a UDP-Socket
 * 
 * It can connect to a remote host, send and receive packets
 * 
 * @package Steam Condenser (PHP)
 * @subpackage Socket
 */
class Socket
{
	/**
	 * @var boolean
	 */
	private $isBlocking = true;
	
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
	public function __construct()
	{
		$this->socketsEnabled = extension_loaded("sockets");
	}
	
  public function __desctruct()
  {
  	$this->close();
  }
	
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
	
	public function connect(InetAddress $ipAddress, $portNumber)
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
      socket_connect($this->socket, $ipAddress, $portNumber);
      
      if($this->isBlocking)
      {
        socket_set_block($this->socket);
      }
      else
      {
        socket_set_nonblock($this->socket);
      }
    }
    else
    {
      if(!$this->socket = fsockopen("udp://$ipAddress", $portNumber, $socketErrno, $socketErrstr, 2))
      {
        throw new Exception("Could not create socket.");
      }
      stream_set_blocking($this->socket, $doBlock);
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
	public function recv($length = 128)
	{
	 if($this->socketsEnabled)
    {
      $data = socket_read($this->socket, $length, PHP_BINARY_READ);
    }
    else
    {
      $data = fread($this->socket, $length);
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