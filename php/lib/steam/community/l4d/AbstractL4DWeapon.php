<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009-2010, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage Steam Community
 */

require_once 'steam/community/GameWeapon.php';

/**
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
abstract class AbstractL4DWeapon extends GameWeapon {

    protected $accuracy;

    protected $headshotsPercentage;

    protected $killPercentage;

    /**
     * Abstract base constructor which parses common data for both, L4DWeapon
     * and L4D2Weapon
     *
     * @param $weaponData
     */
    public function __construct($weaponData) {
        parent::__construct($weaponData);

        $this->accuracy            = (string) $weaponData->accuracy;
        $this->headshotsPercentage = (string) $weaponData->headshots;
        $this->id                  = $weaponData->getName();
        $this->shots               = (int)    $weaponData->shots;
    }

    public function getAccuracy() {
        return $this->accuracy;
    }

    public function getHeadshotsPercentage() {
        return $this->headshotsPercentage;
    }

    public function getKillPercentage() {
        return $this->killPercentage;
    }
}
?>
