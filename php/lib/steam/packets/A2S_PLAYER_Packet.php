<?php
/**
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage Packets
 */

require_once "steam/packets/RequestPacketWithChallenge.php";

/**
 * @package    Steam Condenser (PHP)
 * @subpackage Packets
 */
class A2S_PLAYER_Packet extends RequestPacketWithChallenge
{
	/**
	 * @param long $challengeNumber
	 */
	public function __construct($challengeNumber = "\xFF\xFF\xFF\xFF")
	{
		parent::__construct(SteamPacket::A2S_PLAYER_HEADER, $challengeNumber);
	}
}
?>