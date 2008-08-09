<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage SteamPacket
 * @version $Id$
 */

require_once "steam/packets/SteamPacket.php";

/**
 * @package Steam Condenser (PHP)
 * @subpackage SteamPacket
 */
class A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket extends SteamPacket
{	
	/**
	 * @param long $challengeNumber
	 */
	public function __construct($challengeNumber)
	{
		parent::__construct(SteamPacket::A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER, $challengeNumber);
	}
	
	/**
	 * @return int
	 */
	public function getChallengeNumber()
	{
		return $this->contentData->rewind()->getLong(); 
	}
}
?>