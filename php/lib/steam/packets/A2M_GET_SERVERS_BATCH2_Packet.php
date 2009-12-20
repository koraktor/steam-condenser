<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Source Condenser (PHP)
 * @subpackage Packets
 */

require_once "steam/packets/SteamPacket.php";

/**
 * Represents a request sent to a master server.
 * <br /><br />
 * <a name="filtering">Filtering</a>:<br />
 * Instead of filtering the results sent by the master server locally, you
 * should at least use the following filters to narrow down the results sent by
 * the master server. Receiving all servers from the master server is taking
 * quite some time.<br /><br />
 * Available filters:
 * <ul>
 * 	<li>\type\d: Request only dedicated servers</li>
 *  <li>\secure\1: Request only secure servers</li>
 *  <li>\gamedir\[mod]: Request only servers of a specific mod</li>
 *  <li>\map\[mapname]: Request only servers running a specific map</li>
 *  <li>\linux\1: Request only linux servers</li>
 *  <li>\emtpy\1: Request only <b>non</b>-empty servers</li>
 *  <li>\full\1: Request only servers <b>not</b> full</li>
 *  <li>\proxy\1: Request only spectator proxy servers</li>
 * </ul>
 * @package Source Condenser (PHP)
 * @subpackage Packets
 */
class A2M_GET_SERVERS_BATCH2_Packet extends SteamPacket
{
    private $filter;
    private $regionCode;
    private $startIp;

    /**
     * Creates a master server request, filtering by the given paramters.
     * @param byte regionCode The region code to filter servers by region.
     * @param String startIp This should be the last IP received from the master
     *        server or 0.0.0.0
     * @param String filter The <a href="#filtering">filters</a> to apply in the form ("\filtername\value...")
     */
    public function __construct($regionCode = MasterServer::REGION_ALL, $startIp = "0.0.0.0", $filter = "")
    {
        parent::__construct(SteamPacket::A2M_GET_SERVERS_BATCH2_HEADER);

        $this->filter = $filter;
        $this->regionCode = $regionCode;
        $this->startIp = $startIp;
    }

    /**
     * @return byte[] A byte array representing the contents of this request packet
     */
    public function __toString()
    {
        return chr($this->headerData) . chr($this->regionCode) . $this->startIp . "\0" . $this->filter . "\0";
    }
}
?>
