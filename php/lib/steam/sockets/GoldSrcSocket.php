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
   * @var long
   */
  private $rconChallenge;

  /**
   * @return SteamPacket
   */
  public function getReply()
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

    return SteamPacketFactory::getPacketFromData($packetData);
  }

  /**
   * @param String $password
   * @param String $command
   * @return String
   */
  public function rconExec($password, $command)
  {
    if(empty($this->rconChallenge))
    {
      $this->rconGetChallenge();
    }
     
    $this->rconSend("rcon {$this->rconChallenge} $password $command");
    $response = $this->getReply()->getResponse();
    
    if(trim($response) == "Bad rcon_password." || trim($response) == "You have been banned from this server.")
    {
      throw new RCONNoAuthException();
    }
    
    do
    {
      $this->rconSend("rcon {$this->rconChallenge} $password");
      $responsePart = $this->getReply()->getResponse();
      $response .= $responsePart;
    }
    while($responsePart != "\0\0");
     
    return $response;
  }

  /**
   */
  public function rconGetChallenge()
  {
    $this->rconSend("challenge rcon");
    $response = trim($this->getReply()->getResponse());
    
    if($response == "You have been banned from this server.")
	{
	    throw new RCONNoAuthException();
	}
     
    $this->rconChallenge = intval(substr($response, 15));
  }

  /**
   * @param String $command
   */
  public function rconSend($command)
  {
    $this->send(new RCONGoldSrcRequest($command));
  }
}
?>
