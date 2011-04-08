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

require_once STEAM_CONDENSER_PATH . 'steam/servers/GameServer.php';
require_once STEAM_CONDENSER_PATH . 'steam/sockets/GoldSrcSocket.php';

/**
 * @package    Steam Condenser (PHP)
 * @subpackage GoldSrcServer
 */
class GoldSrcServer extends GameServer {

    /**
     * @var bool
     */
    private $isHLTV;

    /**
     * @param string $address
     * @param int $port The listening port of the server, defaults to 27015
     */
    public function __construct($address, $port = 27015, $isHLTV = false) {
        parent::__construct($address, $port);

        $this->isHLTV = $isHLTV;
    }

    /**
     * Initializes the sockets to communicate with the GoldSrc server
     */
    public function initSocket() {
        $this->socket = new GoldSrcSocket($this->ipAddress, $this->port, $this->isHLTV);
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
