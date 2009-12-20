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

/**
 * Super class for classes representing game weapons
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
abstract class GameWeapon {

    private $avgShotsPerKill;

    protected $kills;

    protected $id;

    protected $shots;

    public function __construct($weaponData) {
        $this->kills = (int) $weaponData->kills;
    }

    /**
     * Returns the average number of shots needed for a kill with this weapon.
     * Calculates the value if needed.
     * @return float
     */
    public function getAvgShotsPerKill() {
        if(empty($this->avgShotsPerKill)) {
            $this->avgShotsPerKill = $this->shots / $this->kills;
        }

        return $this->avgShotsPerKill;
    }

    public function getId() {
        return $this->id;
    }

    public function getKills() {
        return $this->kills;
    }

    public function getShots() {
        return $this->shots;
    }

}
?>
