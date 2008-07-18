<?php
/**
 * @author Sebastian Staudt
 * @version $Id$
 */

class A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket extends SteamPacket
{	
	/**
	 * 
	 */
	public function __construct($challengeNumber)
	{
		parent::__construct(SteamPacket::A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER);
		
		$data = unpack("V", $challengeNumber);
		$this->contentData = $data[1];
	}
	
	/**
	 * @return int
	 */
	public function getChallengeNumber()
	{
		return $this->contentData; 
	}
}
?>