<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php Modified BSD License
 * @package Steam Interface Package (PHP)
 * @subpackage SteamPacket
 * @version $Id$
 */

/**
 * @package Steam Interface Package (PHP)
 * @subpackage SteamPacket
 */
class A2A_PING_ResponsePacket extends SteamPacket
{	
	/**
	 * @param byte[] $contentData
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