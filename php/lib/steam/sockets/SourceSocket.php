<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage SourceSocket
 * @version $Id$
 */

require_once "steam/packets/SteamPacket.php";
require_once "steam/sockets/SteamSocket.php";

/**
 * @package Steam Condenser (PHP)
 * @subpackage SourceSocket
 */
class SourceSocket extends SteamSocket
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
				
				debug("Received packet $packetNumber of $packetCount for request #$requestId");
				
				$bytesRead = $this->receivePacket();
			}
			while($bytesRead > 0 && $this->buffer->getLong() == -2);
			
		  if($isCompressed)
      {
        $packet = SteamPacket::reassemblePacket($splitPackets, true, $uncompressedSize, $packetChecksum);
      }
      else
      {
        $packet = SteamPacket::reassemblePacket($splitPackets);
      }
		}
		else
		{
			$packet = SteamPacket::createPacket($this->buffer->get());
		}
		
		debug("Received packet of type \"" . get_class($packet) . "\"");
		
		return $packet;
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
