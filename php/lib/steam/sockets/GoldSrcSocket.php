<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage GoldSrcSocket
 * @version $Id$
 */

require_once "steam/packets/SteamPacket.php";
require_once "steam/sockets/SteamSocket.php";

/**
 * @package Steam Condenser (PHP)
 * @subpackage GoldSrcSocket
 */
class GoldSrcSocket extends SteamSocket
{
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
        
        debug("Received packet $packetNumber of $packetCount for request #$requestId");
        
        $bytesRead = $this->receivePacket();
      }
      while($bytesRead > 0 && $this->buffer->getLong() == -2);
      
      $packet = SteamPacket::createPacket(implode("", $splitPackets));
    }
    else
    {
      $packet = SteamPacket::createPacket($this->buffer->get());
    }
    
    debug("Received packet of type \"" . get_class($packet) . "\"");
    
    return $packet;
	}
}
?>
