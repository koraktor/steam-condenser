<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 * 
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage RCONExecResponse
 * @version $Id$
 */

require_once "steam/packets/rcon/RCONPacket.php";

class RCONExecResponse extends RCONPacket
{
  public function __construct($requestId, $commandResponse)
  {
    parent::__construct($requestId, RCONPacket::SERVERDATA_RESPONSE_VALUE, $commandResponse);
  }

  public function getResponse()
  {
    return $this->contentData->_array();
  }
}
?>
