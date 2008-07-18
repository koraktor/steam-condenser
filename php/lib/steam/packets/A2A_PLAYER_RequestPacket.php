<?php
/**
 * @author Sebastian Staudt
 * @version $Id$
 */

class A2A_PLAYER_RequestPacket extends SteamPacket
{	
	/**
	 * @param long $challengeNumber
	 */
	public function __construct($challengeNumber = "\xFF\xFF\xFF\xFF")
	{
		parent::__construct(SteamPacket::A2A_PLAYER_REQUEST_HEADER, $challengeNumber);
	}
	
	/**
	 * @return String
	 */
	public function __toString()
	{
		$packetData = pack("cccc", 0xFF, 0xFF, 0xFF, 0xFF);
		$packetData .= pack("cV", $this->headerData, $this->contentData);
		
		return $packetData;
	}
}
?>