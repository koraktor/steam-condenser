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
 * @package Steam Condenser (PHP)
 * @subpackage Packets
 */
class M2C_ISVALIDMD5_Packet extends SteamPacket
{
    /**
     * @var int
     */
    private $challenge;

    public function __construct($data) {
        parent::__construct(SteamPacket::M2C_ISVALIDMD5_HEADER, $data);

        $this->contentData->getByte();
        $this->challenge = $this->contentData->getUnsignedLong();
    }

    public function getChallenge() {
        return $this->challenge;
    }
}
?>
