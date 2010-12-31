<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage Packets
 */

require_once STEAM_CONDENSER_PATH . 'steam/packets/SteamPacket.php';

/**
 * The C2M_CHECKMD5 packet type is used to initialize (challenge) master server
 * communication.
 *
 * @package Steam Condenser (PHP)
 * @subpackage Packets
 */
class C2M_CHECKMD5_Packet extends SteamPacket
{

    /**
     * Creates a new challenge request packet for master server communication
     */
    public function __construct() {
        parent::__construct(SteamPacket::C2M_CHECKMD5_HEADER);
    }

    /**
     * Returns a byte array representation of the packet data
     *
     * @return string A byte array representing the contents of this request
     *         packet
     */
    public function __toString()
    {
        return chr($this->headerData) . "\xFF";
    }

}
?>
