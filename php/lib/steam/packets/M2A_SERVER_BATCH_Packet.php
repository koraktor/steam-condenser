<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Source Condenser (PHP)
 * @subpackage Packets
 */

require_once STEAM_CONDENSER_PATH . 'steam/packets/SteamPacket.php';

/**
 * Represents a response of a master server.
 * @package Source Condenser (PHP)
 * @subpackage Packets
 */
class M2A_SERVER_BATCH_Packet extends SteamPacket
{
	private $serverArray;

	public function __construct($data)
	{
		parent::__construct(SteamPacket::M2A_SERVER_BATCH_HEADER, $data);

		if($this->contentData->getByte() != 10)
		{
			throw new PacketFormatException("Master query response is missing additional 0x0A byte.");
		}

		do
		{
			$firstOctet = $this->contentData->getByte();
			$secondOctet = $this->contentData->getByte();
			$thirdOctet = $this->contentData->getByte();
			$fourthOctet = $this->contentData->getByte();
			$portNumber = $this->contentData->getShort();
			$portNumber = (($portNumber & 0xFF) << 8) + ($portNumber >> 8);
			 
			$this->serverArray[] = "$firstOctet.$secondOctet.$thirdOctet.$fourthOctet:$portNumber";
		}
		while($this->contentData->remaining() > 0);
	}

	public function getServers()
	{
		return $this->serverArray;
	}
}
?>
