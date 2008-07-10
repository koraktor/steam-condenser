<?php
/**
 * @author Sebastian Staudt
 * @version $Id: A2A_PING_ResponsePacket.php 21 2008-02-29 10:39:13Z koraktor $
 */

class A2A_PING_ResponsePacket extends SteamPacket
{	
	/**
	 * 
	 */
	public function __construct($contentData)
	{
		if($contentData != "00000000000000")
		{
			throw new Exception("Wrong formatted A2A_PING Response Packet.");
		}
		parent::__construct(SteamPacket::A2A_PING_RESPONSE_HEADER, $contentData);
	}
}
?>