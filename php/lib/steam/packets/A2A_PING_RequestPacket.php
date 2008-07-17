<?php
/**
 * @author Sebastian Staudt
 * @version $Id: A2A_PING_RequestPacket.php 19 2008-02-28 10:09:21Z koraktor $
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