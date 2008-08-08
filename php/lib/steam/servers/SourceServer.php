<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage SourceServer
 * @version $Id$
 */

/**
 * @package Steam Condenser (PHP)
 * @subpackage SourceServer
 * @todo Server has to recognize incoming packets
 */
class SourceServer extends GameServer
{
	/**
	 * @param InetAddress $serverIP
	 * @param int $portNumber The listening port of the server, defaults to 27015
	 * @since v0.1
	 */
	public function __construct(InetAddress $ipAddress, $portNumber = 27015)
	{
		parent::__construct($portNumber);
		
		$this->socket = new SourceSocket($ipAddress, $portNumber);
	}
}
?>
