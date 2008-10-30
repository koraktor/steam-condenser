<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage UDPSocket
 * @version $Id$
 */

require_once "InetAddress.php";
require_once "Socket.php";

/**
 * This class represents a UDP socket
 *
 * It can connect to a remote host, send and receive packets
 *
 * @package Steam Condenser (PHP)
 * @subpackage UDPSocket
 */
class UDPSocket extends Socket
{
  /**
   * Connects the UDP socket to the host with the given IP address and port number
   */
  public function connect(InetAddress $ipAddress, $portNumber)
  {
    $this->ipAddress = $ipAddress;
    $this->portNumber = $portNumber;

    if($this->socketsEnabled)
    {
      if(!$this->socket = socket_create(AF_INET, SOCK_DGRAM, SOL_UDP))
      {
        $errorCode = socket_last_error($this->socket);
        throw new Exception("Could not create socket: " . socket_strerror($errorCode));
      }
      socket_connect($this->socket, $ipAddress, $portNumber);

      if($this->isBlocking)
      {
        socket_set_block($this->socket);
      }
      else
      {
        socket_set_nonblock($this->socket);
      }
    }
    else
    {
      if(!$this->socket = fsockopen("udp://$ipAddress", $portNumber, $socketErrno, $socketErrstr, 2))
      {
        throw new Exception("Could not create socket.");
      }
      stream_set_blocking($this->socket, $doBlock);
    }
  }
}
?>