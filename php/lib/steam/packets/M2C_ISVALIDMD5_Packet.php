<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010-2011, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage Packets
 */

require_once STEAM_CONDENSER_PATH . 'steam/packets/SteamPacket.php';

/**
 * A packet of type M2S_ISVALIDMD5 is used by the master server to provide a
 * challenge number to a game server
 *
 * @package Steam Condenser (PHP)
 * @subpackage Packets
 */
class M2C_ISVALIDMD5_Packet extends SteamPacket
{

    /**
     * @var int
     */
    private $challenge;

    /**
     * Creates a new response packet with the data from the master server
     *
     * @param string $data The packet data sent by the master server
     */
    public function __construct($data) {
        parent::__construct(SteamPacket::M2C_ISVALIDMD5_HEADER, $data);

        $this->contentData->getByte();
        $this->challenge = $this->contentData->getUnsignedLong();
    }

    /**
     * Returns the challenge number to use for master server communication
     *
     * @return long The challenge number
     */
    public function getChallenge() {
        return $this->challenge;
    }
}
?>
