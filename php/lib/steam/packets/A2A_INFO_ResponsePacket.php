<?php
/**
 * @author Sebastian Staudt
 * @package Steam Interface Package (PHP)
 * @subpackage SteamPacket
 * @version $Id: A2A_INFO_ResponsePacket.php 21 2008-02-29 10:39:13Z koraktor $
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
	public function __construct($contentData)
	{
		if(empty($contentData))
		{
			throw new Exception("Wrong formatted A2A_INFO response packet.");
		}
		parent::__construct(SteamPacket::A2A_INFO_RESPONSE_HEADER);
		
		preg_match('#^' . 
			'(.)' . 
			'(.+)\x00' . 
			'(.+)\x00' . 
			'(.+)\x00' . 
			'(.+)\x00' . 
			'(..)' . 
			'(.)' . 
			'(.)' . 
			'(.)' . 
			'(.)' . 
			'(.)' . 
			'(.)' . 
			'(.)' . 
			'(.+)\x00' . 
			'(.)' . 
			'#U', $contentData, $dataArray);
		
		$this->packetVersion = ord($dataArray[1]);
		$this->serverName = $dataArray[2];
		$this->mapName = $dataArray[3];
		$this->gameDir = $dataArray[4];
		$this->gameName = $dataArray[5];
		$this->appId = hexdec(bin2hex($dataArray[6][1]) . bin2hex($dataArray[6][0]));
		$this->currentPlayers = ord($dataArray[7]);
		$this->maxPlayers = ord($dataArray[8]);
		$this->currentBots = ord($dataArray[9]);
		$this->dedicatedServer = ord($dataArray[10]);
		$this->operatingSystem = $dataArray[11];
		$this->passwordProtected = ord($dataArray[12]);
		$this->secureServer = ord($dataArray[13]);
		$this->gameVersion = $dataArray[14];
		$this->extraDataFlag = ord($dataArray[15]);
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