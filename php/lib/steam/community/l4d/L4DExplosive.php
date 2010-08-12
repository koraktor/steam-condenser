<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage Steam Community
 */

require_once STEAM_CONDENSER_PATH . 'steam/community/GameWeapon.php';

/**
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class L4DExplosive extends GameWeapon {

    /**
     * Creates a new instance of L4DExplosive based on the assigned XML data
     * @param $weaponData
     */
    public function __construct($weaponData) {
        parent::__construct($weaponData);

        $this->id    = $weaponData->getName();
        $this->shots = (int) $weaponData->thrown;
    }

    /**
     * Returns the average number of kills for one shot of this explosive.
     * @return float
     */
    public function getAvgKillsPerShot() {
        return 1 / $this->getAvgShotsPerKill();
    }
}
?>
