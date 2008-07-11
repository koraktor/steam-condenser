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
	public function __construct($challengeNumber)
	{
		/*if(is_int($challengeNumer))
		{
			throw new Exception("A2A_SERVERQUERY_GETCHALLENGE: Challenge number must be of type long.");
		}*/
		parent::__construct(SteamPacket::A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER);
		
		$this->challengeNumber = $challengeNumber;
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