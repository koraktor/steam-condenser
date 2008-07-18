<?php
/**
 * @author Sebastian Staudt
 * @version $Id$
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