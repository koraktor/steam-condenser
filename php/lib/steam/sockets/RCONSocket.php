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
 * @subpackage Sockets
 */

require_once STEAM_CONDENSER_PATH . 'InetAddress.php';
require_once STEAM_CONDENSER_PATH . 'SocketChannel.php';
require_once STEAM_CONDENSER_PATH . 'exceptions/RCONBanException.php';
require_once STEAM_CONDENSER_PATH . 'exceptions/PacketFormatException.php';
require_once STEAM_CONDENSER_PATH . 'steam/packets/rcon/RCONPacket.php';
require_once STEAM_CONDENSER_PATH . 'steam/packets/rcon/RCONPacketFactory.php';
require_once STEAM_CONDENSER_PATH . 'steam/sockets/SteamSocket.php';

/**
 * @package    Steam Condenser (PHP)
 * @subpackage Sockets
 */
class RCONSocket extends SteamSocket {
    public function __construct(InetAddress $ipAddress, $portNumber) {
        parent::__construct($ipAddress, $portNumber);

        $this->buffer = ByteBuffer::allocate(1400);
        $this->channel = SocketChannel::open();
        $this->remoteSocket = array($ipAddress, $portNumber);
    }

    public function send(RCONPacket $dataPacket) {
        if(!$this->channel->isConnected()) {
            $this->channel->connect($this->remoteSocket[0], $this->remoteSocket[1]);
        }

        $this->buffer = ByteBuffer::wrap($dataPacket->getBytes());
        $this->channel->write($this->buffer);
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
