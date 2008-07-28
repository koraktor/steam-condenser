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
	 * @var String
	 */
	private $serverName;
	
	/**
	 * 
	 */
	public function __construct($data)
	{
		parent::__construct(SteamPacket::A2A_INFO_RESPONSE_HEADER, $data);
		
		//@todo implement functionality to parse the data
		/*
		$byteBuffer = new ByteBuffer($data);
		var_dump($byteBuffer->getByte());
		var_dump($byteBuffer->getString());
		var_dump($byteBuffer->getString());
		var_dump($byteBuffer->getString());
		var_dump($byteBuffer->getString());
		var_dump($byteBuffer->getShort());

		$serverInfo["networkVersion"] = $this->getByte();
		$serverInfo["serverName"] = $this->getString();
		$serverInfo["mapName"] = $this->getString();
		$serverInfo["gameDir"] = $this->getString();
		$serverInfo["gameDesc"] = $this->getString();
		$serverInfo["appId"] = $this->getShort();
		$serverInfo["playerNumber"] = $this->getByte();
		$serverInfo["maxPlayers"] = $this->getByte();
		$serverInfo["botNumber"] = $this->getByte();
		$serverInfo["dedicated"] = chr($this->getByte());
		$serverInfo["operatingSystem"] = chr($this->getByte());
		$serverInfo["passwordProtected"] = $this->getByte();
		$serverInfo["secureServer"] = $this->getByte();
		$serverInfo["gameVersion"] = $this->getString();
		$serverInfo["extraDataFlag"] = $this->getByte();
		
		if($serverInfo["extraDataFlag"] & 0x80)
		{
			$serverInfo["serverPort"] = $this->getShort();
		}
		
		if($serverInfo["extraDataFlag"] & 0x40)
		{
			$serverInfo["tvPort"] = $this->getShort();
			$serverInfo["tvName"] = $this->getString();
		}
		
		if($serverInfo["extraDataFlag"] & 0x20)
		{
			$serverInfo["serverTags"] = $this->getString();
		}*/
	}
	
	/**
	 * @return String
	 */
	public function getMapName()
	{
		return $this->mapName;
	}
	
	/**
	 * @return String
	 */
	public function getServerName()
	{
		return $this->serverName;
	}
}
?>
