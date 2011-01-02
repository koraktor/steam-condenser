<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 *
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage SteamSocket
 */

require_once STEAM_CONDENSER_PATH . 'InetAddress.php';
require_once STEAM_CONDENSER_PATH . 'steam/packets/A2M_GET_SERVERS_BATCH2_Packet.php';
require_once STEAM_CONDENSER_PATH . 'steam/packets/C2M_CHECKMD5_Packet.php';
require_once STEAM_CONDENSER_PATH . 'steam/packets/S2M_HEARTBEAT2_Packet.php';
require_once STEAM_CONDENSER_PATH . 'steam/sockets/MasterServerSocket.php';

/**
 * @package Steam Condenser (PHP)
 * @subpackage MasterServer
 */
class MasterServer
{
	const GOLDSRC_MASTER_SERVER = "hl1master.steampowered.com:27010";
	const SOURCE_MASTER_SERVER = "hl2master.steampowered.com:27011";

        const REGION_US_EAST_COAST = 0x00;
	const REGION_US_WEST_COAST = 0x01;
	const REGION_SOUTH_AMERICA = 0x02;
	const REGION_EUROPE = 0x03;
	const REGION_ASIA = 0x04;
	const REGION_AUSTRALIA = 0x05;
	const REGION_MIDDLE_EAST = 0x06;
	const REGION_AFRICA = 0x07;
	const REGION_ALL = 0xFF;

	/**
	 * @var MasterServerSocket
	 */
	private $socket;

	public function __construct($masterServer)
	{
		$masterServer = explode(":", $masterServer);
		$this->socket = new MasterServerSocket(new InetAddress($masterServer[0]), $masterServer[1]);
	}

    /**
     * Request a challenge number from the master server. This is used for
     * further communication with the master server.
     *
     * Please note that this is NOT needed for finding servers using
     * getServers()
     *
     * @return The challenge number returned from the master server
     * @see sendHeartbeat()
     */
    public function getChallenge() {
        $this->socket->send(new C2M_CHECKMD5_Packet());
        return $this->socket->getReply()->getChallenge();
    }


	public function getServers($regionCode = MasterServer::REGION_ALL , $filter = "")
	{
		$failCount  = 0;
		$finished   = false;
		$portNumber = 0;
		$hostName   = "0.0.0.0";

		do
		{
			$this->socket->send(new A2M_GET_SERVERS_BATCH2_Packet($regionCode, "$hostName:$portNumber", $filter));
			try {
				$serverStringArray = $this->socket->getReply()->getServers();

				foreach($serverStringArray as $serverString)
				{
					$serverString = explode(":", $serverString);
					$hostName = $serverString[0];
					$portNumber = $serverString[1];

					if($hostName != "0.0.0.0" && $portNumber != 0)
					{
						$serverArray[] = array($hostName, $portNumber);
					}
					else
					{
						$finished = true;
					}
				}
				$failCount = 0;
			}
			catch(TimeoutException $e) {
				$failCount ++;
				if($failCount == 4) {
			    		throw $e;
				}
			}
		}
		while(!$finished);

		return $serverArray;
	}

    /**
     * Sends a constructed heartbeat to the master server
     *
     * This can be used to check server versions externally.
     *
     * @param array $data The heartbeat data to send to the master server
     * @return SteamPacket[] The reply from the master server – usually zero or
     *         more packets. Zero means either the heartbeat was accepted by
     *         the master or there was a timeout. So usually it's best to
     *         repeat a heartbeat a few times when not receiving any packets.
     */
    public function sendHeartbeat($data) {
        $this->socket->send(new S2M_HEARTBEAT2_Packet($data));

        $replyPackets = array();
        try {
            do {
                $replyPackets[] = $this->socket->getReply();
            } while(true);
        } catch(TimeoutException $e) {}

        return $replyPackets;
    }

}
?>
