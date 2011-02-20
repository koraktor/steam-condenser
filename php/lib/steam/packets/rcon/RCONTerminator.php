<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage RCON
 */

require_once STEAM_CONDENSER_PATH . 'steam/packets/rcon/RCONPacket.php';

/**
 * This class is used to determine the end of a RCON response from Source
 * servers. Packets of this type are sent after the actual RCON command and the
 * empty response packet from the server will indicate the end of the response.
 */
class RCONTerminator extends RCONPacket {

    /**
     * Creates a new RCONTerminator instance for the given request ID
     *
     * @param long $requestId The request ID for this RCON session
     */
    public function __construct($requestId) {
        parent::__construct($requestId, RCONPacket::SERVERDATA_RESPONSE_VALUE);
    }

}
