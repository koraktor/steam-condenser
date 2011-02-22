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

require_once STEAM_CONDENSER_PATH . 'exceptions/RCONBanException.php';
require_once STEAM_CONDENSER_PATH . 'exceptions/RCONNoAuthException.php';
require_once STEAM_CONDENSER_PATH . 'steam/packets/SteamPacket.php';
require_once STEAM_CONDENSER_PATH . 'steam/packets/rcon/RCONGoldSrcRequest.php';
require_once STEAM_CONDENSER_PATH . 'steam/sockets/SteamSocket.php';

/**
 * @package    Steam Condenser (PHP)
 * @subpackage Sockets
 */
class GoldSrcSocket extends SteamSocket
{
    /**
     * @var boolean
     */
    private $isHLTV;

    /**
     * @var long
     */
    private $rconChallenge = -1;

    public function  __construct($ipAddress, $portNumber = 27015, $isHLTV = false)
    {
        parent::__construct($ipAddress, $portNumber);
        $this->isHLTV = $isHLTV;
    }

    /**
     * @return SteamPacket
     */
    public function getReply()
    {
        $bytesRead = $this->receivePacket(1400);

        // Check wether it is a split packet
        if($this->buffer->getLong() == -2)
        {
            do
            {
                $requestId = $this->buffer->getLong();
                $packetCountAndNumber = $this->buffer->getByte();
                $packetCount = $packetCountAndNumber & 0xF;
                $packetNumber = ($packetCountAndNumber >> 4) + 1;

                // Caching of split packet Data
                $splitPackets[$packetNumber - 1] = $this->buffer->get();

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

            $packet = SteamPacketFactory::reassemblePacket($splitPackets);
        }
        else
        {
            $packet = SteamPacketFactory::getPacketFromData($this->buffer->get());
        }

        trigger_error("Received packet of type \"" . get_class($packet) . "\"");

        return $packet;
    }

    /**
     * @param String $password
     * @param String $command
     * @return String
     */
    public function rconExec($password, $command)
    {
        if($this->rconChallenge == -1 || $this->isHLTV)
        {
            $this->rconGetChallenge();
        }

        $this->rconSend("rcon {$this->rconChallenge} $password $command");
        $this->rconSend("rcon {$this->rconChallenge} $password");
        if($this->isHLTV) {
            try {
                $response = $this->getReply()->getResponse();
            }
            catch(TimeoutException $e) {
                $response = "";
            }
        }
        else {
            $response = $this->getReply()->getResponse();
        }

        if(trim($response) == "Bad rcon_password.") {
            throw new RCONNoAuthException();
        } elseif(trim($response) == "You have been banned from this server.") {
            throw new RCONBanException();
        }

        do {
            $responsePart = $this->getReply()->getResponse();
            $response .= $responsePart;
        } while(strlen($responsePart) > 0);

        return $response;
    }

    /**
     */
    public function rconGetChallenge()
    {
        $this->rconSend("challenge rcon");
        $response = trim($this->getReply()->getResponse());

        if($response == "You have been banned from this server.") {
            throw new RCONBanException();
        }

        $this->rconChallenge = floatval(substr($response, 14));
    }

    /**
     * @param String $command
     */
    public function rconSend($command)
    {
        $this->send(new RCONGoldSrcRequest($command));
    }
}
?>
