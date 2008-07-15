<?php
/**
 * @author Sebastian Staudt
 * @package Steam Interface Package (PHP)
 * @subpackage SteamPacket
 * @version $Id: SteamPacket.php 21 2008-02-29 10:39:13Z koraktor $
 */

/**
 * @package Steam Interface Package (PHP)
 * @subpackage SteamPacket
 */
abstract class SteamPacket
{
	const A2A_INFO_REQUEST_HEADER = 0x54;
	const A2A_INFO_RESPONSE_HEADER = 0x49;
	const A2A_PING_REQUEST_HEADER = 0x69;
	const A2A_PING_RESPONSE_HEADER = 0x6A;
	const A2A_PLAYER_REQUEST_HEADER = 0x55;
	const A2A_PLAYER_RESPONSE_HEADER = 0x44;
	const A2A_RULES_REQUEST_HEADER = 0x56;
	const A2A_RULES_RESPONSE_HEADER = 0x45;
	const A2A_SERVERQUERY_GETCHALLENGE_REQUEST_HEADER = 0x57;
	const A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER = 0x41;
	
	/**
	 * This variable stores the content of the package
	 * @var mixed
	 */
	protected $contentData;
	
	/**
	 * This byte stores the type of the packet
	 * @var byte
	 */
	protected $headerData;
	
	/**
	 * @return SteamPacket
	 */
	public static function createPacket($rawData)
	{
		$header = ord($rawData[0]);
		$data = substr($rawData, 1);
		
		switch($header)
		{
			case SteamPacket::A2A_INFO_REQUEST_HEADER:
				return new A2A_INFO_RequestPacket();
				break;
				
			case SteamPacket::A2A_INFO_RESPONSE_HEADER:
				return new A2A_INFO_ResponsePacket($data);
				break;
			
			case SteamPacket::A2A_PING_REQUEST_HEADER:
				return new A2A_PING_RequestPacket();
				break;
				
			case SteamPacket::A2A_PING_RESPONSE_HEADER:
				return new A2A_PING_ResponsePacket($data);
				break;
				
			case SteamPacket::A2A_PLAYER_REQUEST_HEADER:
				return new A2A_PLAYER_ResponsePacket();
				break;
			
			case SteamPacket::A2A_PLAYER_RESPONSE_HEADER:
				return new A2A_PLAYER_ResponsePacket($data);
				break;
				
			case SteamPacket::A2A_RULES_REQUEST_HEADER:
				return new A2A_RULES_RequestPacket();
				break;
			
			case SteamPacket::A2A_RULES_RESPONSE_HEADER:
				return new A2A_RULES_ResponsePacket($data);
				break;
				
			case SteamPacket::A2A_SERVERQUERY_GETCHALLENGE_REQUEST_HEADER:
				return new A2A_SERVERQUERY_GETCHALLENGE_RequestPacket();
				break;
				
			case SteamPacket::A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER:
				return new A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket($data);
				break;
				
			default:
				throw new Exception("Unknown packet with header 0x" . dechex($header) . " received.");
		}
	}
	
	/**
	 * @param byte $headerData
	 * @param mixed $contentData
	 * @param bool $splitPacket
	 */
	public function __construct($headerData, $contentData = null)
	{
		$this->headerData = $headerData;
		$this->contentData = $contentData;
	}
	
	/**
	 * 
	 */
	public function getData()
	{
		return $this->contentData;
	}
	
	/**
	 * @return byte
	 */
	public function getHeader()
	{
		return $this->headerData;
	}
	
	/**
	 * @return String
	 * @todo Automatically split packets based on size
	 */
	public function __toString()
	{
		$packetData = pack("ccc", 0xFF, 0xFF, 0xFF);
		
		/*if($this->splitPacket)
		{
			$packetData .= pack("c", 0xFE);
		}
		else*/
		{
			$packetData .= pack("c", 0xFF);
		}
		
		$packetData .= pack("ca*", $this->headerData, $this->contentData);
		
		return $packetData;
	}
}
?>