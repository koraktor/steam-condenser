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
 * The S2A_LOGSTRING packet type is used to transfer log messages.
 *
 * @package Steam Condenser (PHP)
 * @subpackage Packets
 */
class S2A_LOGSTRING_Packet extends SteamPacket
{

    /**
     * @var string The log message contained in this packet
     */
    private $message;

    /**
     * Creates a new log message packet
     *
     * @param string $data
     */
    public function __construct($data) {
        parent::__construct(SteamPacket::S2A_LOGSTRING_HEADER, $data);

        $this->contentData->getByte();
        $this->message = $this->contentData->getString();
    }

    /**
     * @return string The log message of this packet
     */
    public function getMessage() {
        return $this->message;
    }

}
?>
