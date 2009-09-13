<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage GoldSrcServer
 */

require_once "InetAddress.php";
require_once "steam/servers/GameServer.php";
require_once "steam/sockets/GoldSrcSocket.php";

/**
 * @package    Steam Condenser (PHP)
 * @subpackage GoldSrcServer
 */
class GoldSrcServer extends GameServer {

    /**
     * Splits the player status obtained with "rcon status"
     * @param String $playerStatus
     * @return String[]
     */
    public function splitPlayerStatus($playerStatus) {
        preg_match('%# *(\d+) +"(.*)" +(\d+) +(.*)%', $playerStatus, $playerData);
        array_shift($playerData);
        $morePlayerData = preg_split('/\s+/', array_pop($playerData));
        $playerData = array_merge($playerData, $morePlayerData);
        $playerData[0] = $playerData[2];
        unset($playerData[2]);
        unset($playerData[5]);
        return array_values($playerData);
    }

    /**
     * @param InetAddress $serverIP
     * @param int $portNumber The listening port of the server, defaults to 27015
     */
    public function __construct(InetAddress $ipAddress, $portNumber = 27015, $isHLTV = false) {
        parent::__construct($portNumber);

        $this->socket = new GoldSrcSocket($ipAddress, $portNumber, $isHLTV);
    }

    /**
     * @param String $password
     * @return boolean
     */
    public function rconAuth($password) {
        $this->rconPassword = $password;
        return true;
    }

    /**
     * @param String $command
     * @return String
     */
    public function rconExec($command) {
        return trim($this->socket->rconExec($this->rconPassword, $command));
    }
}
?>
