<?php
/**
 * @author Sebastian Staudt
 * @package Steam Interface Package (PHP)
 * @subpackage SteamSocket
 * @version $Id: SteamSocket.php 21 2008-02-29 10:39:13Z koraktor $
 */

/**
 * @package Steam Interface Package (PHP)
 * @subpackage SteamSocket
 */
class SteamSocket extends Socket
{
	/**
	 * 
	 */
	private function createA2A_PING_ResponsePacket()
	{
		return new A2A_PING_ResponsePacket($this->getString());
	}
	
	/**
	 * 
	 */
	private function createA2A_INFO_ResponsePacket()
	{
		$serverInfo["networkVersion"] = $this->getByte();
		$serverInfo["serverName"] = $this->getString();
		$serverInfo["mapName"] = $this->getString();
		$serverInfo["gameDir"] = $this->getString();
		$serverInfo["gameDesc"] = $this->getString();
		$serverInfo["appId"] = $this->getShort();
		$serverInfo["playerNumber"] = $this->getByte();
		$serverInfo["maxPlayers"] = $this->getByte();
		$serverInfo["botNumber"] = $this->getByte();
		$serverInfo["dedicated"] = chr($this->getByte());
		$serverInfo["operatingSystem"] = chr($this->getByte());
		$serverInfo["passwordProtected"] = $this->getByte();
		$serverInfo["secureServer"] = $this->getByte();
		$serverInfo["gameVersion"] = $this->getString();
		$serverInfo["extraDataFlag"] = $this->getByte();
		
		if($serverInfo["extraDataFlag"] & 0x80)
		{
			$serverInfo["serverPort"] = $this->getShort();
		}
		
		if($serverInfo["extraDataFlag"] & 0x40)
		{
			$serverInfo["tvPort"] = $this->getShort();
			$serverInfo["tvName"] = $this->getString();
		}
		
		if($serverInfo["extraDataFlag"] & 0x20)
		{
			$serverInfo["serverTags"] = $this->getString();
		}
		
		return new A2A_INFO_ResponsePacket($serverInfo);
	}
	
	/**
	 */
	private function createA2A_SERVERQUERY_GETCHALLENGE_ResponsePacket()
	{
		return new A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket($this->getLong());
	}
	
	/**
	 * Receives a packet from the connected socket
	 * @return SteamPacket
	 * @var int $length
	 * @todo Check correctness of algorithm
	 * @todo Switch between Source and GoldSrc packets
	 * @todo Regard compressed packets
	 */
	public function getReply()
	{
		$this->readToBuffer(1400);
		
		var_dump($this->readBuffer);
		
		$splitPacketHeader = $this->getLong();
		
		if($splitPacketHeader == -2)
		{
			$requestId = $this->getLong();
			$packetNumber = $this->getByte();
			$packetCount = $this->getByte();
			$splitSize = $this->getShort();
			$splitPackets[$packetNumber] = $this->readBuffer;
			
			var_dump($requestId, $packetNumber, $packetCount, $splitSize);
		}
		
		$packetHeader = $this->getByte();
		
		switch($packetHeader)
		{
			case SteamPacket::A2A_INFO_REQUEST_HEADER:
				return new A2A_INFO_RequestPacket();
				break;
				
			case SteamPacket::A2A_INFO_RESPONSE_HEADER:
				return $this->createA2A_INFO_ResponsePacket();
				break;
			
			case SteamPacket::A2A_PING_REQUEST_HEADER:
				return new A2A_PING_RequestPacket();
				break;
				
			case SteamPacket::A2A_PING_RESPONSE_HEADER:
				return $this->createA2A_PING_ResponsePacket();
				break;
				
			case SteamPacket::A2A_PLAYER_REQUEST_HEADER:
				return new A2A_PLAYER_ResponsePacket();
				break;
			
			case SteamPacket::A2A_PLAYER_RESPONSE_HEADER:
				return new A2A_PLAYER_ResponsePacket($packetData["packetContent"]);
				break;
				
			case SteamPacket::A2A_RULES_REQUEST_HEADER:
				return new A2A_RULES_RequestPacket();
				break;
			
			case SteamPacket::A2A_RULES_RESPONSE_HEADER:
				return new A2A_RULES_ResponsePacket($packetData["packetContent"]);
				break;
				
			case SteamPacket::A2A_SERVERQUERY_GETCHALLENGE_REQUEST_HEADER:
				return new A2A_SERVERQUERY_GETCHALLENGE_RequestPacket();
				break;
				
			case SteamPacket::A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER:
				return $this->createA2A_SERVERQUERY_GETCHALLENGE_ResponsePacket();
				break;
				
			default:
				throw new Exception("Unknown packet with header 0x" . dechex($packetHeader) . " received.");
		}
	}
}
?>