<?php
/**
 * @author Sebastian Staudt
 * @version $Id$
 */

class A2A_PING_RequestPacket extends SteamPacket
{	
	/**
	 * 
	 */
	public function __construct()
	{
		parent::__construct(SteamPacket::A2A_PING_REQUEST_HEADER);
	}
}
?>