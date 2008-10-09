<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage RCONAuthRequest
 * @version $Id$
 */

require_once "steam/packets/rcon/RCONPacket.php";

class RCONAuthRequest extends RCONPacket
{
  public function __construct($requestId, $password)
  {
    parent::__construct($requestId, RCONPacket::SERVERDATA_AUTH, $password);
  }
}
?>
