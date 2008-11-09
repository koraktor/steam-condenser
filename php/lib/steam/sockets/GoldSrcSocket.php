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

require_once "steam/packets/SteamPacket.php";
require_once "steam/packets/rcon/RCONGoldSrcRequest.php";
require_once "steam/sockets/SteamSocket.php";

/**
 * @package    Steam Condenser (PHP)
 * @subpackage Sockets
 */
class GoldSrcSocket extends SteamSocket
{
  /**
   * @var int
   */
  private $rconChallenge;

  /**
   * @return SteamPacket
   */
  public function getReplyData()
  {
    $bytesRead = $this->receivePacket(1400);

    // Check wether it is a split packet
    if($this->buffer->getLong() == -2)
    {
      do
      {
        $requestId = $this->buffer->getLong();
        $packetCountAndNumber = $this->buffer->getByte();
        $packetCount = $packetCountAndNumber & 0xF;
        $packetNumber = ($packetCountAndNumber >> 4) + 1;
        // Omit additional header on the first packet
        if($packetNumber == 1)
        {
          $this->buffer->getLong();
        }
        $splitPackets[$packetNumber] = $this->buffer->get();

        trigger_error("Received packet $packetNumber of $packetCount for request #$requestId");

        $bytesRead = $this->receivePacket();
      }
      while($bytesRead > 0 && $this->buffer->getLong() == -2);

      $packetData = implode("", $splitPackets);
    }
    else
    {
      $packetData = $this->buffer->get();
    }

    return $packetData;
  }

  public function rconExec($password, $command)
  {
    if(empty($this->rconChallenge))
    {
      $this->rconGetChallenge();
    }
     
    $this->rconSend("rcon {$this->rconChallenge} $password $command");
     
    return $this->getReply()->getResponse();
  }

  public function rconGetChallenge()
  {
    $this->rconSend("challenge rcon");
    $bytesRead = $this->receivePacket(1400);
     
    if($bytesRead == 0)
    {
      throw new NothingReceivedException();
    }
     
    $this->rconChallenge = intval(substr($this->buffer->_array(), 19, 10));
  }

  public function rconSend($command)
  {
    $this->send(new RCONGoldSrcRequest($command));
  }
}
?>
