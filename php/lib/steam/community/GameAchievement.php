<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 * @version $Id$
 */

/**
 * The GameAchievement class represents a specific achievement for a single game
 * and for a single user
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class GameAchievement
{
  /**
   * Creates the achievement with the given name for the given user and game and
   * marks it as already done if set to true
   * @param $steamId
   * @param $appId
   * @param $name
   * @param $done
   */
  public function __construct($steamId, $appId, $name, $done)
  {
    $this->appId   = $appId;
    $this->done    = $done;
    $this->name    = $name;
    $this->steamId = $steamId;
  }
}
?>
