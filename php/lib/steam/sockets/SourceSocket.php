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
	public function getReply()
	{
		$bytesRead = $this->receivePacket(1400);

		// Check wether it is a split packet
		if($this->buffer->getLong() == -2)
		{
			do
			{
				$requestId = $this->buffer->getLong();
				$isCompressed = (($requestId & 0x80000000) != 0);
				$packetCount = $this->buffer->getByte();
				$packetNumber = $this->buffer->getByte() + 1;

				if($isCompressed)
				{
					$splitSize = $this->buffer->getLong();
					$packetChecksum = $this->buffer->getLong();
				}
				else
				{
					$splitSize = $this->buffer->getShort();
				}

				$splitPackets[$packetNumber] = $this->buffer->get();

				trigger_error("Received packet $packetNumber of $packetCount for request #$requestId");

				$bytesRead = $this->receivePacket();
			}
			while($bytesRead > 0 && $this->buffer->getLong() == -2);
			 
			if($isCompressed)
			{
				$packet = SteamPacketFactory::reassemblePacket($splitPackets, true, $packetChecksum);
			}
			else
			{
				$packet = SteamPacketFactory::reassemblePacket($splitPackets);
			}
		}
		else
		{
			$packet = SteamPacketFactory::getPacketFromData($this->buffer->get());
		}

		return $packet;
	}
}
?>
