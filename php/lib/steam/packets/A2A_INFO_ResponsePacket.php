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
class A2A_INFO_ResponsePacket extends SteamPacket
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
	 * 
	 */
	public function __construct($data)
	{
		parent::__construct(SteamPacket::A2A_INFO_RESPONSE_HEADER, $data);
		
		$byteBuffer = new ByteBuffer($data);
		$this->networkVersion = $byteBuffer->getByte();
		$this->serverName = $byteBuffer->getString();
		$this->mapName = $byteBuffer->getString();
		$this->gameDir = $byteBuffer->getString();
		$this->gameDesc = $byteBuffer->getString();
		$this->appId = $byteBuffer->getShort();
		$this->playerNumber = $byteBuffer->getByte();
		$this->maxPlayers = $byteBuffer->getByte();
		$this->botNumber = $byteBuffer->getByte();
		$this->dedicated = chr($byteBuffer->getByte());
		$this->operatingSystem = chr($byteBuffer->getByte());
		$this->passwordProtexted = $byteBuffer->getByte();
		$this->secureServer = $byteBuffer->getByte();
		$this->gameVersion = $byteBuffer->getString();
		$extraDataFlag = $byteBuffer->getByte();
		
		if($extraDataFlag & 0x80)
		{
			$this->serverPort = $byteBuffer->getShort();
		}
		
		if($extraDataFlag & 0x40)
		{
		  $this->tvPort = $byteBuffer->getShort();
		  $this->tvName = $byteBuffer->getString();
		}
		
	 if($extraDataFlag & 0x20)
    {
      $this->serverTags = $byteBuffer->getString();
    }
	}
	
	/**
	 * @return mixed[]
	 */
	public function getInfoHash()
	{
		return array_diff_key(get_object_vars($this), array("contentData" => null, "headerData" => null));
	}
}
?>
