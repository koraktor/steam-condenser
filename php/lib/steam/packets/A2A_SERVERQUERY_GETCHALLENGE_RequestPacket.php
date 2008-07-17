<?php
/**
 * @author Sebastian Staudt
 * @version $Id: A2A_SERVERQUERY_GETCHALLENGE_RequestPacket.php 21 2008-02-29 10:39:13Z koraktor $
 */

class A2A_SERVERQUERY_GETCHALLENGE_RequestPacket extends SteamPacket
{	
	/**
	 * 
	 */
	public function __construct()
	{
		parent::__construct(SteamPacket::A2A_SERVERQUERY_GETCHALLENGE_REQUEST_HEADER);
	}
}
?>