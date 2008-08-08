<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage GoldSrcSocket
 * @version $Id$
 */

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
		// Read the first packet into the buffer
		// @todo Use ByteBuffer functionality here
		$this->readToBuffer(1400);
		
		// Check wether it is a split packet
		if($this->getLong() == -2)
		{
			do
			{
				$requestId = $this->getLong();
				$packetCount = $this->getByte();
				$packetNumber = $this->getByte() + 1;
				$splitSize = $this->getShort();
				// Omit additional header on the first packet 
				if($packetNumber == 1)
				{
					$this->getLong();
				}
				$splitPackets[$packetNumber] = $this->flushBuffer();
				
				if($packetNumber < $packetCount)
				{
					$this->readToBuffer(1400);
				}
				
				trigger_error("Received packet $packetNumber of $packetCount for request #$requestId");
			}
			while($packetNumber < $packetCount && $this->getLong() == -2);
			
			return SteamPacket::createPacket(implode("", $splitPackets));
		}
		else
		{
			return SteamPacket::createPacket($this->flushBuffer());
		}
	}
}
?>
