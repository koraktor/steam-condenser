<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage RCONAuthResponse
 * @version $Id$
 */

require_once "steam/packets/rcon/RCONPacket.php";

class RCONAuthResponse extends RCONPacket
{
  public function __construct($requestId)
  {
    parent::__construct($requestId, RCONPacket::SERVERDATA_AUTH_RESPONSE);
  }
}
?>
