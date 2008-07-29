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
abstract class RequestPacketWithChallenge extends SteamPacket
{
	/**
	 * Returns a packed version of the packet data
	 * @return String
	 */
	public function __toString()
	{
	  return pack("cccccV", 0xFF, 0xFF, 0xFF, 0xFF, $this->headerData, $this->contentData);;
	}
}
?>