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
 * @subpackage SourceServer
 */

require_once STEAM_CONDENSER_PATH . 'InetAddress.php';
require_once STEAM_CONDENSER_PATH . 'exceptions/RCONNoAuthException.php';
require_once STEAM_CONDENSER_PATH . 'steam/packets/rcon/RCONAuthRequest.php';
require_once STEAM_CONDENSER_PATH . 'steam/packets/rcon/RCONAuthResponse.php';
require_once STEAM_CONDENSER_PATH . 'steam/packets/rcon/RCONExecRequest.php';
require_once STEAM_CONDENSER_PATH . 'steam/packets/rcon/RCONTerminator.php';
require_once STEAM_CONDENSER_PATH . 'steam/servers/GameServer.php';
require_once STEAM_CONDENSER_PATH . 'steam/sockets/RCONSocket.php';
require_once STEAM_CONDENSER_PATH . 'steam/sockets/SourceSocket.php';

/**
 * @package Steam Condenser (PHP)
 * @subpackage SourceServer
 * @todo Server has to recognize incoming packets
 */
class SourceServer extends GameServer
{
    /**
     * @var long
     */
    private $rconRequestId;

    /**
     * @param InetAddress $serverIP
     * @param int $portNumber The listening port of the server, defaults to 27015
     */
    public function __construct(InetAddress $ipAddress, $portNumber = 27015) {
        parent::__construct($portNumber);

        $this->rconSocket = new RCONSocket($ipAddress, $portNumber);
        $this->socket = new SourceSocket($ipAddress, $portNumber);
    }

    public function rconAuth($password) {
        $this->rconRequestId = rand(0, pow(2, 16));

        $this->rconSocket->send(new RCONAuthRequest($this->rconRequestId, $password));
        $this->rconSocket->getReply();
        $reply = $this->rconSocket->getReply();

        return $reply->getRequestId() == $this->rconRequestId;
    }

    public function rconExec($command) {
        $this->rconSocket->send(new RCONExecRequest($this->rconRequestId, $command));
        $this->rconSocket->send(new RCONTerminator($this->rconRequestId));

        do {
            $responsePacket = $this->rconSocket->getReply();

            if($responsePacket == null) {
                continue;
            }

            if($responsePacket instanceof RCONAuthResponse) {
                throw new RCONNoAuthException();
            }

            $responsePackets[] = $responsePacket;
        } while($responsePacket == null || strlen($responsePacket->getResponse()) > 0);

        $response = '';
        foreach($responsePackets as $packet) {
            $response .= $packet->getResponse();
        }

        return trim($response);
    }
}
?>
