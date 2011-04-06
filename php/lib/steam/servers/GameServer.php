<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage GameServer
 */

require_once STEAM_CONDENSER_PATH . 'exceptions/SteamCondenserException.php';
require_once STEAM_CONDENSER_PATH . 'exceptions/TimeoutException.php';
require_once STEAM_CONDENSER_PATH . 'steam/packets/A2S_INFO_Packet.php';
require_once STEAM_CONDENSER_PATH . 'steam/packets/A2S_PLAYER_Packet.php';
require_once STEAM_CONDENSER_PATH . 'steam/packets/A2S_RULES_Packet.php';
require_once STEAM_CONDENSER_PATH . 'steam/packets/A2S_SERVERQUERY_GETCHALLENGE_Packet.php';

/**
 * @package    Steam Condenser (PHP)
 * @subpackage GameServer
 */
abstract class GameServer {

    const REQUEST_CHALLENGE = 0;
    const REQUEST_INFO      = 1;
    const REQUEST_PLAYER    = 2;
    const REQUEST_RULES     = 3;

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
    protected $playerHash;

    /**
     * @var mixed[]
     */
    protected $rulesHash;

    /**
     * @var SteamSocket
     */
    protected $socket;

        /**
     * Parses the player attribute names supplied by +rcon status+
     *
     * @param string statusHeader
     * @return Split player attribute names
     */
    private static function getPlayerStatusAttributes($statusHeader) {
        $statusAttributes = array();
        foreach(preg_split("/\s+/", $statusHeader) as $attribute) {
            if($attribute == '#') {
                $statusAttributes[] = 'id';
            } else if($attribute == 'connected') {
                $statusAttributes[] = 'time';
            } else if($attribute == 'frag') {
                $statusAttributes[] = 'score';
            } else {
                $statusAttributes[] = $attribute;
            }
        }

        return $statusAttributes;
    }

    /**
     * Splits the player status obtained with "rcon status"
     *
     * @param array attributes
     * @param string playerStatus
     * @return Split player data
     */
    private static function splitPlayerStatus($attributes, $playerStatus) {
        $data = explode('"', trim(substr($playerStatus, 1)));
        $data = array_merge(
            preg_split("/\s+/", trim($data[0])),
            array($data[1]),
            preg_split("/\s+/", trim($data[2]))
        );

        if(sizeof($attributes) > sizeof($data) + 1) {
            array_splice($data, 1, 0, array(null));
            array_splice($data, 4, 0, array(null, null, null, null));
        }

        $playerData = array();
        for($i = 0; $i < sizeof($data); $i ++) {
            $playerData[$attributes[$i]] = $data[$i];
        }

        return $playerData;
    }

    /**
     * @param InetAddress $serverIP
     * @param int $portNumber The listening port of the server, defaults to 27015
     */
    public function __construct($portNumber = 27015) {
        if(!is_numeric($portNumber) || $portNumber <= 0 || $portNumber > 65535) {
            throw new Exception("The listening port of the server has to be a number greater than 0 and less than 65536.");
        }
    }

    /**
     * @return The response time of this server in milliseconds
     */
    public function getPing() {
        if($this->ping == null) {
            $this->updatePing();
        }
        return $this->ping;
    }

    /**
     * @return An array of SteamPlayers representing all players on this server
     */
    public function getPlayers($rconPassword = null) {
        if($this->playerHash == null) {
            $this->updatePlayerInfo($rconPassword);
        }
        return $this->playerHash;
    }

    /**
     * @return A associative array containing the rules of this server
     */
    public function getRules() {
        if($this->rulesHash == null) {
            $this->updateRulesInfo();
        }
        return $this->rulesHash;
    }

    /**
     * @return A associative array containing basic information about the server
     */
    public function getServerInfo() {
        if($this->infoHash == null) {
            $this->updateServerInfo();
        }
        return $this->infoHash;
    }

    public function initialize() {
        $this->updatePing();
        $this->updateServerInfo();
        $this->updateChallengeNumber();
    }

    /**
     * @return SteamPacket
     */
    private function getReply() {
        return $this->socket->getReply();
    }

    /**
     *
     */
    private function handleResponseForRequest($requestType, $repeatOnFailure = true) {
        try {
            switch($requestType) {
                case self::REQUEST_CHALLENGE:
                    $expectedResponse = "S2C_CHALLENGE_Packet";
                    $requestPacket    = new A2S_SERVERQUERY_GETCHALLENGE_Packet();
                    break;
                case self::REQUEST_INFO:
                    $expectedResponse = "S2A_INFO_BasePacket";
                    $requestPacket    = new A2S_INFO_Packet();
                    break;
                case self::REQUEST_PLAYER:
                    $expectedResponse = "S2A_PLAYER_Packet";
                    $requestPacket    = new A2S_PLAYER_Packet($this->challengeNumber);
                    break;
                case self::REQUEST_RULES:
                    $expectedResponse = "S2A_RULES_Packet";
                    $requestPacket    = new A2S_RULES_Packet($this->challengeNumber);
                    break;
                default:
                    throw new SteamCondenserException("Called with wrong request type.");
            }

            $this->sendRequest($requestPacket);

            $responsePacket = $this->getReply();

            switch(get_class($responsePacket)) {
                case "S2A_INFO_DETAILED_Packet":
                case "S2A_INFO2_Packet":
                    $this->infoHash = $responsePacket->getInfoHash();
                    break;
                case "S2A_PLAYER_Packet":
                    $this->playerHash = $responsePacket->getPlayerHash();
                    break;
                case "S2A_RULES_Packet":
                    $this->rulesHash = $responsePacket->getRulesArray();
                    break;
                case "S2C_CHALLENGE_Packet":
                    $this->challengeNumber = $responsePacket->getChallengeNumber();
                    break;
                default:
                    throw new SteamCondenserException("Response of type " . get_class($responsePacket) . " cannot be handled by this method.");
            }

            if(!is_a($responsePacket, $expectedResponse)) {
                trigger_error("Expected {$expectedResponse}, got " . get_class($responsePacket) . ".");
                if($repeatOnFailure) {
                    $this->handleResponseForRequest($requestType, false);
                }
            }
        }
        catch(TimeoutException $e) {
            trigger_error("Expected {$expectedResponse}, but timed out. The server is probably offline.");
        }
    }

    abstract public function rconAuth($password);

    abstract public function rconExec($command);

    /**
     * @param SteamPacket $requestData
     */
    private function sendRequest(SteamPacket $requestData) {
        $this->socket->send($requestData);
    }

    /**
     *
     */
    public function updateChallengeNumber() {
        $this->handleResponseForRequest(self::REQUEST_CHALLENGE);
    }

    /**
     *
     */
    public function updatePing() {
        $this->sendRequest(new A2S_INFO_Packet());
        $startTime = microtime(true);
        $this->getReply();
        $endTime = microtime(true);
        $this->ping = intval(round(($endTime - $startTime) * 1000));

        return $this->ping;
    }

    /**
     *
     */
    public function updatePlayerInfo($rconPassword = null) {
        $this->handleResponseForRequest(self::REQUEST_PLAYER);

        if($rconPassword != null && !empty($this->playerHash)) {
            $this->rconAuth($rconPassword);
            $players = array();
            foreach(explode("\n", $this->rconExec('status')) as $line) {
                if(strpos($line, '#') === 0 && $line != '#end') {
                    $players[] = $line;
                }
            }
            $attributes = self::getPlayerStatusAttributes(array_shift($players));

            foreach($players as $player) {
                $playerData = self::splitPlayerStatus($attributes, $player);
                if(array_key_exists($playerData['name'], $this->playerHash)) {
                    $this->playerHash[$playerData['name']]->addInformation($playerData);
                }
            }
        }
    }

    /**
     *
     */
    public function updateRulesInfo() {
        $this->handleResponseForRequest(self::REQUEST_RULES);
    }

    /**
     *
     */
    public function updateServerInfo() {
        $this->handleResponseForRequest(self::REQUEST_INFO);
    }

    /**
     * @return String
     */
    public function __toString() {
        $returnString = "";

        $returnString .= "Ping: {$this->ping}\n";
        $returnString .= "Challenge number: {$this->challengeNumber}\n";

        if($this->infoHash != null) {
            $returnString .= "Info:\n";
            foreach($this->infoHash as $key => $value) {
                if(is_array($value)) {
                    $returnString .= "  {$key}:\n";
                    foreach($value as $subKey => $subValue) {
                        $returnString .= "    {$subKey} = {$subValue}\n";
                    }
                }
                else {
                    $returnString .= "  {$key}: {$value}\n";
                }
            }
        }

        if($this->playerHash != null) {
            $returnString .= "Players:\n";
            foreach($this->playerHash as $player) {
                $returnString .= "  {$player}\n";
            }
        }

        if($this->rulesHash != null) {
            $returnString .= "Rules:\n";
            foreach($this->rulesHash as $key => $value) {
                $returnString .= "  {$key}: {$value}\n";
            }
        }

        return $returnString;
    }
}
?>
