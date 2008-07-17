<?php
/**
 * @author Sebastian Staudt
 * @version $Id: A2A_RULES_RequestPacket.php 21 2008-02-29 10:39:13Z koraktor $
 */

class A2A_RULES_RequestPacket extends SteamPacket
{	
	/**
	 * @param long $challengeNumber
	 */
	public function __construct($challengeNumber = "\xFF\xFF\xFF\xFF")
	{
		parent::__construct(SteamPacket::A2A_RULES_REQUEST_HEADER, $challengeNumber);
	}
	
	/**
	 * @return String
	 */
	public function __toString()
	{
		$packetData = pack("c4", 0xFF, 0xFF, 0xFF, 0xFF);
		$packetData .= pack("cV", $this->headerData, $this->contentData);
		
		return $packetData;
	}
}
?>