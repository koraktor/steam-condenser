<?php
/**
 * @author Sebastian Staudt
 * @version $Id: A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket.php 21 2008-02-29 10:39:13Z koraktor $
 */

class A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket extends SteamPacket
{	
	/**
	 * @var int
	 */
	private $challengeNumber;
	
	/**
	 * 
	 */
	public function __construct($contentData)
	{
		if(empty($contentData))
		{
			throw new Exception("Wrong formatted A2A_SERVERQUERY_GETCHALLENGE response packet.");
		}
		parent::__construct(SteamPacket::A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER);
		
		$contentData = unpack("VchallengeNumber", $contentData);
		$this->challengeNumber = $contentData["challengeNumber"];
	}
	
	/**
	 * @return int
	 */
	public function getChallengeNumber()
	{
		return $this->challengeNumber; 
	}
}
?>