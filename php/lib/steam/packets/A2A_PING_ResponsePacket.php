<?php
/**
 * @author Sebastian Staudt
 * @version $Id$
 */

class A2A_PING_ResponsePacket extends SteamPacket
{	
	/**
	 * 
	 */
	public function __construct($contentData)
	{
		if($contentData != "00000000000000\0")
		{
			throw new Exception("Wrong formatted A2A_PING Response Packet.");
		}
		parent::__construct(SteamPacket::A2A_PING_RESPONSE_HEADER, $contentData);
	}
}
?>