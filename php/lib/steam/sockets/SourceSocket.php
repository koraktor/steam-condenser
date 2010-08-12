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

require_once STEAM_CONDENSER_PATH . 'steam/packets/SteamPacketFactory.php';
require_once STEAM_CONDENSER_PATH . 'steam/sockets/SteamSocket.php';

/**
 * @package    Steam Condenser (PHP)
 * @subpackage Sockets
 */
class SourceSocket extends SteamSocket
{
    /**
     * @return byte[]
     */
    public function getReply()
    {
        $bytesRead = $this->receivePacket(1400);
        $isCompressed = false;

        // Check wether it is a split packet
        if($this->buffer->getLong() == -2) {
            do {
                $requestId = $this->buffer->getLong();
                $isCompressed = (($requestId & 0x80000000) != 0);
                $packetCount = $this->buffer->getByte();
                $packetNumber = $this->buffer->getByte() + 1;

                if($isCompressed) {
                    $splitSize = $this->buffer->getLong();
                    $packetChecksum = $this->buffer->getUnsignedLong();
                }
                else {
                    $splitSize = $this->buffer->getShort();
                }

                // Caching of split packet Data
                $splitPackets[$packetNumber] = $this->buffer->get();

                trigger_error("Received packet $packetNumber of $packetCount for request #$requestId");

                // Receiving the next packet
                if(sizeof($splitPackets) < $packetCount) {
                    try {
                        $bytesRead = $this->receivePacket();
                    }
                    catch(TimeoutException $e) {
                        $bytesRead = 0;
                    }
                }
                else {
                    $bytesRead = 0;
                }
            }
            while($bytesRead > 0 && $this->buffer->getLong() == -2);

            if($isCompressed) {
                $packet = SteamPacketFactory::reassemblePacket($splitPackets, true, $packetChecksum);
            }
            else {
                $packet = SteamPacketFactory::reassemblePacket($splitPackets);
            }
        }
        else {
            $packet = SteamPacketFactory::getPacketFromData($this->buffer->get());
        }

        if($isCompressed) {
            trigger_error("Received compressed reply of type \"" . get_class($packet) . "\"");
        }
        else {
            trigger_error("Received reply of type \"" . get_class($packet) . "\"");
        }

        return $packet;
    }
}
?>
