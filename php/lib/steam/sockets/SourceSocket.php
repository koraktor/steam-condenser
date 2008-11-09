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

require_once "steam/packets/SteamPacketFactory.php";
require_once "steam/sockets/SteamSocket.php";

/**
 * @package    Steam Condenser (PHP)
 * @subpackage Sockets
 */
class SourceSocket extends SteamSocket
{
  /**
   * @return byte[]
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
        $isCompressed = $this->packetIsCompressed($requestId);

        $packetCount = $this->buffer->getByte();
        $packetNumber = $this->buffer->getByte() + 1;
        $splitSize = $this->buffer->getShort();

        if($isCompressed)
        {
          $uncompressedSize = $this->buffer->getShort();
          $packetChecksum = $this->buffer->getLong();
        }

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
      	
      if($isCompressed)
      {
        $packet = SteamPacketFactory::reassemblePacket($splitPackets, true, $uncompressedSize, $packetChecksum);
      }
      else
      {
        $packet = SteamPacketFactory::reassemblePacket($splitPackets);
      }
    }
    else
    {
      $packetData = $this->buffer->get();
    }

    return $packetData;
  }

  /**
   * @return boolean
   */
  private function packetIsCompressed($requestId)
  {
    return ($requestId & 0x8000) != 0;
  }
}
?>
