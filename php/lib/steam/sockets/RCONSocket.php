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
 * @subpackage Sockets
 */

require_once STEAM_CONDENSER_PATH . 'InetAddress.php';
require_once STEAM_CONDENSER_PATH . 'TCPSocket.php';
require_once STEAM_CONDENSER_PATH . 'exceptions/RCONBanException.php';
require_once STEAM_CONDENSER_PATH . 'exceptions/PacketFormatException.php';
require_once STEAM_CONDENSER_PATH . 'steam/packets/rcon/RCONPacket.php';
require_once STEAM_CONDENSER_PATH . 'steam/packets/rcon/RCONPacketFactory.php';
require_once STEAM_CONDENSER_PATH . 'steam/sockets/SteamSocket.php';

/**
 * @package    Steam Condenser (PHP)
 * @subpackage Sockets
 */
class RCONSocket extends SteamSocket
{
    /**
     * @var InetAddress
     */
    private $ipAddress;

    /**
     * @var int
     */
    private $portNumber;


    public function __construct(InetAddress $ipAddress, $portNumber) {
        $this->buffer = ByteBuffer::allocate(1400);
        $this->ipAddress = $ipAddress;
        $this->portNumber = $portNumber;
    }

    /**
     * Closes the underlying TCPSocket if it exists
     *
     * @see SteamSocket#close
     */
    public function close() {
        if(!empty($this->socket)) {
            parent::close();
        }
    }

    public function send(RCONPacket $dataPacket) {
        if(empty($this->socket)) {
            $this->socket = new TCPSocket();
            $this->socket->connect($this->ipAddress, $this->portNumber);
        }

        $this->socket->send($dataPacket->__toString());
    }

    public function getReply() {
        if($this->receivePacket(1440) == 0) {
            throw new RCONBanException();
        }
        $packetData = substr($this->buffer->_array(), 0, $this->buffer->limit());
        $packetSize = $this->buffer->getLong() + 4;

        if($packetSize > 1440) {
            $remainingBytes = $packetSize - $this->buffer->limit();
            do {
                if($remainingBytes < 1440) {
                    $this->receivePacket($remainingBytes);
                }
                else {
                    $this->receivePacket(1440);
                }
                $packetData .= substr($this->buffer->_array(), 0, $this->buffer->limit());
                $remainingBytes -= $this->buffer->limit();
            }
            while($remainingBytes > 0);
        }

        return RCONPacketFactory::getPacketFromData($packetData);
    }
}
?>
