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
 * The S2M_HEARTBEAT2 packet type is used to signal a game servers availability
 * and status to the master servers.
 *
 * @package Steam Condenser (PHP)
 * @subpackage Packets
 */
class S2M_HEARTBEAT2_Packet extends SteamPacket
{
    /**
     * @var array
     */
    private $serverData;

    /**
     * Creates a new heartbeat packet to send to a master server
     *
     * @param array $data The server data to send with the heartbeat to the
     *        master server
     */
    public function __construct($data = array()) {
        $defaultData = array(
            'appid'     => 320,
            'bots'      => 0,
            'challenge' => null,
            'dedicated' => 0,
            'gamedir'   => 'hl2mp',
            'gameport'  => 27015,
            'gametype'  => 'ctf',
            'lan'       => 1,
            'map'       => 'null',
            'max'       => 24,
            'os'        => 'l',
            'password'  => 0,
            'players'   => 0,
            'product'   => 'hl2dm',
            'protocol'  => 7,
            'region'    => 255,
            'secure'    => 0,
            'specport'  => 0,
            'type'      => 'd',
            'version'   => '1.0.0.0'
        );
        $this->serverData = array_merge($defaultData, $data);

        if(empty($data['challenge'])) {
            throw new SteamCondenserException("You have to provide a challenge number when sending a heartbeat to a master server.");
        }

        parent::__construct(SteamPacket::S2M_HEARTBEAT2_HEADER);
    }

    /**
     * Returns a byte array representation of the packet data
     *
     * @return string A byte array representing the contents of this request
     *         packet
     */
    public function __toString() {
        $string = chr($this->headerData) . "\x0A";

        while(list($k, $v) = each($this->serverData)) {
            $string .= "\\$k\\$v";
        }

        return $string . "\x0A";
    }
}
?>
