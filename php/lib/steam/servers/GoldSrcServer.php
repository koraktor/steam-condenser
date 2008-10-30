<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage GoldSrcServer
 * @version $Id$
 */

require_once "InetAddress.php";
require_once "steam/servers/GameServer.php";
require_once "steam/sockets/GoldSrcSocket.php";

/**
 * @package Steam Condenser (PHP)
 * @subpackage GoldSrcServer
 * @todo Server has to recognize incoming packets
 */
class GoldSrcServer extends GameServer
{
  /**
   * @param InetAddress $serverIP
   * @param int $portNumber The listening port of the server, defaults to 27015
   * @since v0.1
   */
  public function __construct(InetAddress $ipAddress, $portNumber = 27015)
  {
    parent::__construct($portNumber);

    $this->socket = new GoldSrcSocket($ipAddress, $portNumber);
  }

  public function rconAuth($password)
  {
    $this->rconPassword = $password;
    return true;
  }

  public function rconExec($command)
  {
    return $this->socket->rconExec($this->rconPassword, $command);
  }
}
?>
