<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage RCONSocket
 * @version $Id$
 */

require_once "ByteBuffer.php";
require_once "InetAddress.php";
require_once "SocketChannel.php";
require_once "exceptions/PacketFormatException.php";
require_once "steam/packets/rcon/RCONAuthResponse.php";
require_once "steam/packets/rcon/RCONExecResponse.php";
require_once "steam/packets/rcon/RCONPacket.php";
require_once "steam/sockets/SteamSocket.php";

class RCONSocket extends SteamSocket
{
  public function __construct(InetAddress $ipAddress, $portNumber)
  {
    parent::__construct($ipAddress, $portNumber);
    
    $this->buffer = ByteBuffer::allocate(1400);
    $this->channel = SocketChannel::open();
    $this->remoteSocket = array($ipAddress, $portNumber);
  }
  
  public function createPacket()
  {
    $byteBuffer = new ByteBuffer($this->buffer->_array());
    
    $packetSize = $byteBuffer->getLong();
    $requestId = $byteBuffer->getLong();
    $header = $byteBuffer->getLong();
    $data = $byteBuffer->getString();
    
    switch($header)
    {
      case RCONPacket::SERVERDATA_AUTH_RESPONSE:
        return new RCONAuthResponse($requestId);
      case RCONPacket::SERVERDATA_RESPONSE_VALUE:
        return new RCONExecResponse($requestId, $data);
      default:
        throw new PacketFormatException("Unknown packet with header " . dechex($header) . " received.");
    }
  }
  
  public function send(RCONPacket $dataPacket)
  {
    if(!$this->channel->isConnected())
    {
      $this->channel->connect($this->remoteSocket[0], $this->remoteSocket[1]);
    }
    
    $this->buffer = ByteBuffer::wrap($dataPacket->getBytes());
    $this->channel->write($this->buffer);
  }
  
  public function getReply()
  {
    $this->buffer = ByteBuffer::allocate(1400);
    $this->channel->read($this->buffer);
    
    return $this->createPacket();
  }
}
?>
