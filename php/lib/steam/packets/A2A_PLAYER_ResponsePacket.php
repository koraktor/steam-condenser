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
		parent::__construct(SteamPacket::A2A_PLAYER_RESPONSE_HEADER, $contentData);
		
		$this->contentData->getByte();
		
		while($this->contentData->remaining() > 0)
		{
			$playerData = array($this->contentData->getByte(), $this->contentData->getString(), $this->contentData->getLong(), $this->contentData->getFloat());
      $this->playerArray[$playerData[0]] = new SteamPlayer($playerData[0], $playerData[1], $playerData[2], $playerData[3]);			
		}
	}
	
	/**
	 * @return SteamPlayer[]
	 */
	public function getPlayerArray()
	{
		return $this->playerArray;
	}
}
?>
