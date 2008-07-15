<?php
/**
 * @author Sebastian Staudt
 * @package Steam Interface Package (PHP)
 * @subpackage SteamSocket
 * @version $Id: SteamSocket.php 21 2008-02-29 10:39:13Z koraktor $
 */

/**
 * @package Steam Interface Package (PHP)
 * @subpackage SteamSocket
 */
class SteamSocket extends Socket
{
	/**
	 * Receives a packet from the connected socket
	 * @return SteamPacket
	 * @var int $length
	 * @todo Check correctness of algorithm
	 * @todo Switch between Source and GoldSrc packets
	 * @todo Regard compressed packets
	 */
	public function getReply()
	{
		$replyPacket = $this->readPacket();
		
		var_dump($replyPacket);
		
		trigger_error("Got reply of type \"" . get_class($replyPacket) . "\".");
		
		return $replyPacket;
	}
	
	/**
	 * @return SteamPacket
	 */
	public function readPacket()
	{
		// Read the first packet into the buffer
		$this->readToBuffer(1400);
		
		var_dump($this->readBuffer);
		
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
	
	/**
	 *
	 */
	public function send(SteamPacket $dataPacket)
	{
		trigger_error("Sending packet of type \"" . get_class($dataPacket) . "\"...");
		parent::send($dataPacket);
	}
}
?>