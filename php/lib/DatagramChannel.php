<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage DatagramChannel
 * @version $Id$
 */

require_once "ByteBuffer.php";
require_once "InetAddress.php";
require_once "Socket.php";

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
		$this->socket = new Socket();
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