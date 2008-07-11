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
	public function __construct($serverInfo)
	{
		if(!is_array($serverInfo))
		{
			throw new Exception("Parameter 1 should be an array.");
		}
		parent::__construct(SteamPacket::A2A_INFO_RESPONSE_HEADER);
		
		foreach($serverInfo as $infoKey => $infoValue)
		{
			$this->$infoKey = $infoValue;
		}
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