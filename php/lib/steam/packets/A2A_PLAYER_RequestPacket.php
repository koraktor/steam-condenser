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
class A2A_PLAYER_RequestPacket extends RequestPacketWithChallenge
{	
	/**
	 * @param long $challengeNumber
	 */
	public function __construct($challengeNumber = "\xFF\xFF\xFF\xFF")
	{
		parent::__construct(SteamPacket::A2A_PLAYER_REQUEST_HEADER, $challengeNumber);
	}
}
?>