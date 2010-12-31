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
 * The M2S_REQUESTRESTART packet type is used to by master servers to request a
 * game server restart, e.g. when using outdated versions.
 *
 * @package Steam Condenser (PHP)
 * @subpackage Packets
 */
class M2S_REQUESTRESTART_Packet extends SteamPacket
{

    /**
     * @var long
     */
    private $challenge;

    /**
     * Creates a new server restart request packet sent by a master server
     *
     * @param string $data This packet returns the challenge number initially
     *        provided by an M2C_ISVALIDMD5 packet.
     */
    public function __construct($data) {
        parent::__construct(SteamPacket::C2M_CHECKMD5_HEADER, $data);

        $this->challenge = $this->contentData->getUnsignedLong();
    }

    /**
     * Returns the challenge number used for master server communication
     *
     * @return long The challenge number
     */
    public function getChallenge()
    {
        return $this->challenge;
    }

}
?>
