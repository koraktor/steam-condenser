<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage GameServer
 * @version $Id$
 */

/**
 * @package Steam Condenser (PHP)
 * @subpackage GameServer
 * @todo Server has to recognize incoming packets
 */
class GameServer
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
	 * @var SteamPlayer[]
	 */
	private $playerArray;
	
	/**
	 * @var mixed[]
	 */
	private $rulesHash;
	
	/**
	 * @var SteamSocket
	 */
	protected $socket;
	
	/**
	 * @param InetAddress $serverIP
	 * @param int $portNumber The listening port of the server, defaults to 27015
	 * @since v0.1
	 */
	public function __construct($portNumber = 27015)
	{
		if(!is_numeric($portNumber) || $portNumber <= 0 || $portNumber > 65535)
		{
			throw new Exception("The listening port of the server has to be a number greater than 0 and less than 65536.");
		}
	}
	
	/**
   * @return The response time of this server in milliseconds
   */
	public function getPing()
	{
		return $this->ping;
	}
	
	/**
   * @return An array of SteamPlayers representing all players on this server
   */
	public function getPlayers()
	{
		return $this->playerArray;
	}
	
	/**
   * @return A associative array containing the rules of this server
   */
	public function getRules()
	{
		return $this->rulesHash;
	}
	
	/**
   * @return A associative array containing basic information about the server
   */
	public function getServerInfo()
	{
		return $this->infoHash;
	}
	
	public function initialize()
	{
		$this->updatePing();
		$this->updateServerInfo();
		$this->updateChallengeNumber();
	}
	
	/**
	 * 
	 */
	public function updatePing() 
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
	public function updateChallengeNumber()
	{
		$this->sendRequest(new A2A_SERVERQUERY_GETCHALLENGE_RequestPacket());
		$this->challengeNumber = $this->getReply()->getChallengeNumber();
	}
	
 /**
   * 
   */
  public function updatePlayerInfo()
  {
    $this->sendRequest(new A2A_PLAYER_RequestPacket($this->challengeNumber));
    $this->playerArray = $this->getReply()->getPlayerArray();
  }
  
  /**
   * 
   */
  public function updateRulesInfo()
  {
    $this->sendRequest(new A2A_RULES_RequestPacket($this->challengeNumber));
    $this->rulesHash = $this->getReply()->getRulesArray();
  }
	
	/**
	 * 
	 */
	public function updateServerInfo()
	{
		$this->sendRequest(new A2A_INFO_RequestPacket());
		$this->infoHash = $this->getReply()->getInfoHash();
	}
	
	/**
	 * @return SteamPacket
	 */
	private function getReply()
	{
		return $this->socket->getReply();
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
