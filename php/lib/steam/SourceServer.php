<?php
/**
 * @author Sebastian Staudt
 * @package Steam Interface Package (PHP)
 * @subpackage SourceServer
 * @version $Id: SourceServer.php 21 2008-02-29 10:39:13Z koraktor $
 */

/**
 * @package Steam Interface Package (PHP)
 * @subpackage SourceServer
 * @todo Server has to recognize incoming packets
 */
class SourceServer
{
	/**
	 * @var int
	 */
	private $challengeNumber;
	
	/**
	 * @var InetAddress
	 */
	private $ipAddress;
	
	/**
	 * @var String
	 */
	private $mapName;
	
	/**
	 * @var int
	 */
	private $ping;
	
	/**
	 * @var int
	 */
	private $portNumber;
	
	/**
	 * @var String
	 */
	private $serverName;
	
	/**
	 * @var SteamSocket
	 */
	private $socket;
	
	/**
	 * @param InetAddress $serverIP
	 * @param int $portNumber The listening port of the server, defaults to 27015
	 * @since v0.1
	 */
	public function __construct(InetAddress $ipAddress, $portNumber = 27015)
	{
		if(!is_numeric($portNumber) || $portNumber <= 0)
		{
			throw new Exception("The listening port of the server has to be a number greater than 0.");
		}
		
		$this->ipAddress = $ipAddress;
		$this->portNumber = $portNumber;
		$this->socket = new SteamSocket($this->ipAddress, $this->portNumber);
	}
	
	public function initialize()
	{
		$this->getPing();
		$this->getServerInfo();
		$this->getChallengeNumber();
	}
	
	
	/**
	 * 
	 */
	public function getPing() 
	{
		$this->sendRequest(new A2A_PING_RequestPacket());
		$startTime = microtime(true);
		$this->getReply();
		$endTime = microtime(true);
		$this->ping = intval(round(($endTime - $startTime) * 1000));
		
		return $this->ping;
	}
	
	/**
	 * 
	 */
	public function getChallengeNumber()
	{
		$this->sendRequest(new A2A_SERVERQUERY_GETCHALLENGE_RequestPacket());
		$this->challengeNumber = $this->getReply()->getChallengeNumber();
	}
	
	/**
	 * 
	 */
	public function getServerInfo()
	{
		$this->sendRequest(new A2A_INFO_RequestPacket());
		$this->parseServerInfo($this->getReply());
	}
	
	/**
	 * 
	 */
	public function getPlayerInfo()
	{
		$this->sendRequest(new A2A_PLAYER_RequestPacket($this->challengeNumber));
		var_dump($this->getReply());
	}
	
	/**
	 * 
	 */
	public function getRulesInfo()
	{
		$this->sendRequest(new A2A_RULES_RequestPacket($this->challengeNumber));
		$this->rulesArray = $this->getReply()->getRulesArray();
	}
	
	/**
	 * @return SteamPacket
	 */
	private function getReply()
	{
		return $this->socket->getReply();
	}
	
	/**
	 * @param A2A_PLAYER_ResponsePacket $playerResponse
	 */
	private function parsePlayerInfo(A2A_PLAYER_ResponsePacket $playerResponse)
	{
		$this->playerArray = $playerResponse->getPlayers();
	}
	
	/**
	 * 
	 */
	private function parseServerInfo(A2A_INFO_ResponsePacket $infoResponse)
	{
		$this->mapName = $infoResponse->getMapName();
		$this->serverName = $infoResponse->getServerName();
	}
	
	/**
	 * @param SteamPacket $requestData
	 */
	private function sendRequest(SteamPacket $requestData)
	{
		$this->socket->send($requestData);
	}
}
?>