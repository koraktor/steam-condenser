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
class A2A_PLAYER_ResponsePacket extends SteamPacket
{	
	/**
	 * @var SteamPlayer[] 
	 */
	private $playerArray;
	
	/**
	 * @param mixed $contentData
	 */
	public function __construct($contentData)
	{
		if(empty($contentData))
		{
			throw new Exception("Wrong formatted A2A_PLAYER response packet.");
		}
		parent::__construct(SteamPacket::A2A_PLAYER_RESPONSE_HEADER);
		
		$numberOfPlayers = hexdec(bin2hex($contentData[0]));
		$contentData = substr($contentData, 1);
		
		while(strlen($contentData) > 0)
		{
			$zeroByte = strpos($contentData, 0x00);
			$endByte = $zeroByte + 8;
			$playerData = unpack("cplayerId/a{$zeroByte}playerName/VplayerPoints/fplayerConnectTime", $contentData);
			$contentData = substr($contentData, $endByte + 1);
			
			$this->playerArray[] = new SteamPlayer(
				intval($playerData["playerId"]),
				$playerData["playerName"],
				intval($playerData["playerPoints"]),
				floatval($playerData["playerConnectTime"])
			);
		}
	}
	
	/**
	 * @return SteamPlayer[]
	 */
	public function getPlayers()
	{
		return $this->playerArray;
	}
}
?>