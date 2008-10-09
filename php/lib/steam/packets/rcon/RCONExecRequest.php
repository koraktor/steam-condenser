<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage RCONExecRequest
 * @version $Id$
 */

require_once "steam/packets/rcon/RCONPacket.php";

class RCONExecRequest extends RCONPacket
{
  public function __construct($requestId, $command)
  {
    parent::__construct($requestId, RCONPacket::SERVERDATA_EXECCOMMAND, $command);
  }
}
?>
