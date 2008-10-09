<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage RCONNoAuthException
 * @version $Id$
 */

class RCONNoAuthException extends SteamCondenserException
{
  public function __construct()
  {
    parent::__construct("Not authenticated yet.");
  }
}
?>
