<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 * 
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage Sockets
 * @version    $Id$
 */

require_once "InetAddress.php";
require_once "SocketChannel.php";
require_once "exceptions/PacketFormatException.php";
require_once "steam/packets/rcon/RCONPacket.php";
require_once "steam/packets/rcon/RCONPacketFactory.php";
require_once "steam/sockets/SteamSocket.php";

/**
 * @package    Steam Condenser (PHP)
 * @subpackage Sockets
 */
class RCONSocket extends SteamSocket
{
  public function __construct(InetAddress $ipAddress, $portNumber)
  {
    parent::__construct($ipAddress, $portNumber);

    $this->buffer = ByteBuffer::allocate(1400);
    $this->channel = SocketChannel::open();
    $this->remoteSocket = array($ipAddress, $portNumber);
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

    return RCONPacketFactory::getPacketFromData($this->buffer->_array());
  }
}
?>
