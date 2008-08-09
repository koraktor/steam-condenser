<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage A2A_INFO_ResponsePacket
 * @version $Id$
 */

require_once "steam/packets/SteamPacket.php";

/**
 * @package Steam Condenser (PHP)
 * @subpackage A2A_INFO_ResponsePacket
 */
abstract class A2A_INFO_ResponsePacket extends SteamPacket
{	
	/**
	 * @var String
	 */
	private $mapName;
	
	/**
	 * @var int
	 */
	private $networkVersion;
	
	/**
	 * @var String
	 */
	private $serverName;
	
	/**
	 * @return mixed[]
	 */
	public function getInfoHash()
	{
		return array_diff_key(get_object_vars($this), array("contentData" => null, "headerData" => null));
	}
}
?>
