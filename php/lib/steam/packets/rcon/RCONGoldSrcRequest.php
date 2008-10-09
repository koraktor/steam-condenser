<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage RCONGoldSrcRequest
 * @version $Id$
 */

require_once "steam/packets/SteamPacket.php";

class RCONGoldSrcRequest extends SteamPacket
{
  public function __construct($request)
  {
    parent::__construct(0x00, $request);
  }
  
  public function getBytes()
  {
    return pack("Va*", 0xFFFFFFFF, $this->contentData->_array());
  }
}
?>
