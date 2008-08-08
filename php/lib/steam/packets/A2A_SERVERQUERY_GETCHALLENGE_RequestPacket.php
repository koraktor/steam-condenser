<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage SteamPacket
 * @version $Id$
 */

/**
 * @package Steam Condenser (PHP)
 * @subpackage SteamPacket
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