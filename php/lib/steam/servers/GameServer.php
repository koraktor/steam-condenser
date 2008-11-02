<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 * 
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage GameServer
 * @version $Id$
 */

require_once "steam/packets/A2S_INFO_Packet.php";
require_once "steam/packets/A2A_PING_Packet.php";
require_once "steam/packets/A2S_PLAYER_Packet.php";
require_once "steam/packets/A2S_RULES_Packet.php";
require_once "steam/packets/A2S_SERVERQUERY_GETCHALLENGE_Packet.php";

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
  protected $challengeNumber;

  /**
   * @var mixed[]
   */
  protected $infoHash;

  /**
   * @var int
   */
  protected $ping;

  /**
   * @var SteamPlayer[]
   */
  protected $playerArray;

  /**
   * @var mixed[]
   */
  protected $rulesHash;

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
    if($this->ping == null)
    {
      $this->updatePing();
    }
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
    $this->sendRequest(new A2A_PING_Packet());
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
    $this->sendRequest(new A2S_SERVERQUERY_GETCHALLENGE_Packet());
    $this->challengeNumber = $this->getReply()->getChallengeNumber();
  }

  /**
   *
   */
  public function updatePlayerInfo()
  {
    $this->sendRequest(new A2S_PLAYER_Packet($this->challengeNumber));
    $this->playerArray = $this->getReply()->getPlayerArray();
  }

  /**
   *
   */
  public function updateRulesInfo()
  {
    $this->sendRequest(new A2S_RULES_Packet($this->challengeNumber));
    $this->rulesHash = $this->getReply()->getRulesArray();
  }

  /**
   *
   */
  public function updateServerInfo()
  {
    $this->sendRequest(new A2S_INFO_Packet());
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

  /**
   * @return String
   */
  public function __toString()
  {
    $returnString = "";

    $returnString .= "Ping: {$this->ping}\n";
    $returnString .= "Challenge number: {$this->challengeNumber}\n";

    if($this->infoHash != null)
    {
      $returnString .= "Info:\n";
      foreach($this->infoHash as $key => $value)
      {
        if(is_array($value))
        {
          $returnString .= "  {$key}:";
          foreach($value as $subKey => $subValue)
          {
            $returnString .= " {$subKey} = {$subValue}";
          }
        }
        else
        {
          $returnString .= "  {$key}: {$value}\n";
        }
      }
    }

    if($this->playerArray != null)
    {
      $returnString .= "Players:\n";
      foreach($this->playerArray as $player)
      {
        $returnString .= "  {$player}\n";
      }
    }

    if($this->rulesHash != null)
    {
      $returnString .= "Rules:\n";
      foreach($this->rulesHash as $key => $value)
      {
        $returnString .= "  {$key}: {$value}\n";
      }
    }

    return $returnString;
  }
}
?>
