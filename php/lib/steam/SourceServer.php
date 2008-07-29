<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php Modified BSD License
 * @package Steam Interface Package (PHP)
 * @subpackage SourceServer
 * @version $Id$
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
	 * @var mixed[]
	 */
	private $infoHash;
	
	/**
	 * @var int
	 */
	private $ping;
	
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
		$this->infoHash = $this->getReply()->getInfoHash();
	}
	
	/**
	 * 
	 */
	public function getPlayerInfo()
	{
		$this->sendRequest(new A2A_PLAYER_RequestPacket($this->challengeNumber));
		$this->parsePlayerInfo($this->getReply());
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
	 * @param SteamPacket $requestData
	 */
	private function sendRequest(SteamPacket $requestData)
	{
		$this->socket->send($requestData);
	}
}
?>
