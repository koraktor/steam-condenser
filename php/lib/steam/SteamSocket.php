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
	 * Receives a packet from the connected socket
	 * @return SteamPacket
	 * @var int $length
	 * @todo Check correctness of algorithm
	 * @todo Switch between Source and GoldSrc packets
	 * @todo Regard compressed packets
	 */
	public function getReply()
	{
		$replyData = parent::getReply(1400);
		
		if(strlen($replyData) > 10)
		{
			$packetData = unpack("VsplitPacket/VrequestId/cpacketCount/cpacketNumber/vsplitSize/VpacketStart/cpacketHeader/a*packetContent", $replyData);
			if($packetData["splitPacket"] == -2)
			{	
				$header = $packetData["packetHeader"];
				$splitPackets[$packetData["packetNumber"]] = $packetData["packetContent"];
			
				while($packetData["packetNumber"] < $packetData["packetCount"] - 1)
				{
					$packetData = parent::getReply(1400);
					$packetData = unpack("VsplitPacket/VrequestId/cpacketCount/cpacketNumber/vsplitSize/a*packetContent", $packetData);
					$splitPackets[$packetData["packetNumber"]] = $packetData["packetContent"];
				}
	
				$replyData = pack("Vca*", -1, $header, implode("\0", $splitPackets));
			}
		}
		
		$packetData = unpack("VpacketStart/cpacketHeader/a*packetContent", $replyData);
		
		switch($packetData["packetHeader"])
		{
			case SteamPacket::A2A_INFO_REQUEST_HEADER:
				return new A2A_INFO_RequestPacket();
				break;
				
			case SteamPacket::A2A_INFO_RESPONSE_HEADER:
				return new A2A_INFO_ResponsePacket($packetData["packetContent"]);
				break;
			
			case SteamPacket::A2A_PING_REQUEST_HEADER:
				return new A2A_PING_RequestPacket();
				break;
				
			case SteamPacket::A2A_PING_RESPONSE_HEADER:
				return new A2A_PING_ResponsePacket($packetData["packetContent"]);
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
				return new A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket($packetData["packetContent"]);
				break;
				
			default:
				throw new Exception("Unknown packet with header 0x" . dechex($packetData["packetHeader"]) . " received.");
		}
	}
}
?>