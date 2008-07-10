<?php
/**
 * @author Sebastian Staudt
 * @package Source Interface Package (PHP)
 * @subpackage InetAddress
 * @version $Id: InetAddress.php 21 2008-02-29 10:39:13Z koraktor $
 */

/**
 * @package Source Interface Package (PHP)
 * @since v0.1
 * @subpackage InetAddress
 */
class InetAddress
{
	/**
	 * This array saves the octets of the IP address
	 * @var int[4]
	 */
	private $octets = array(127, 0, 0, 1);
	
	/**
	 * @param String $inetAddress
	 * @since v0.1
	 */
	public function __construct($inetAddress = null)
	{
		if(!is_string($inetAddress))
		{
			throw new Exception("Parameter has to be a String.");
		}
		
		$octets = explode(".", $inetAddress);
		if(sizeof($octets) != 4)
		{
			throw new Exception("Wrong formatted IP address.");
		}
		
		foreach($octets as $octet)
		{
			if(!is_numeric($octet) | $octet < 0 || $octet > 255)
			{
				throw new Exception("Wrong formatted IP address.");
			}
		}
		
		$this->octets = $octets;
	}
	
	/**
	 * @return String 
	 */
	public function __toString()
	{
		return implode(".", $this->octets);
	}
}
?>