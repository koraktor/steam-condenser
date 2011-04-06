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
 * @subpackage GoldSrcServer
 */

require_once STEAM_CONDENSER_PATH . 'InetAddress.php';
require_once STEAM_CONDENSER_PATH . 'steam/servers/GameServer.php';
require_once STEAM_CONDENSER_PATH . 'steam/sockets/GoldSrcSocket.php';

/**
 * @package    Steam Condenser (PHP)
 * @subpackage GoldSrcServer
 */
class GoldSrcServer extends GameServer {

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
