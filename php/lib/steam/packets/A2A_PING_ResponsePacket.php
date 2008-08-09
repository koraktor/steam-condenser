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
class A2A_PING_ResponsePacket extends SteamPacket
{	
	/**
	 * @param byte[] $contentData
	 */
	public function __construct($contentData)
	{
		if($contentData != "\0" && $contentData != "00000000000000\0")
		{
			throw new Exception("Wrong formatted A2A_PING Response Packet.");
		}
		parent::__construct(SteamPacket::A2A_PING_RESPONSE_HEADER, $contentData);
	}
}
?>