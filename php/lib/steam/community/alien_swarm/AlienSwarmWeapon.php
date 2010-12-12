<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage Steam Community
 */

require_once STEAM_CONDENSER_PATH . 'steam/community/GameWeapon.php';

/**
 * AlienSwarmWeapon holds statistical information about weapons used by a
 * player in Alien Swarm.
 */
class AlienSwarmWeapon extends GameWeapon {

    private $accuracy;

    private $damage;

    private $friendlyFire;

    private $name;

    /**
     * Creates a new instance of AlienSwarmWeapon based on the assigned weapon
     * XML data
     *
     * @param  $weaponData
     */
    public function __construct($weaponData) {
        parent::__construct($weaponData);

        $this->accuracy     = (float) $weaponData->accuracy;
        $this->damage       = (int) $weaponData->damage;
        $this->friendlyFire = (int) $weaponData->friendlyfire;
        $this->name         = (string) $weaponData->name;
        $this->shots        = (int) $weaponData->shotsfired;
    }

    public function getAccuracy() {
        return $this->accuracy;
    }

    public function getDamage() {
        return $this->damage;
    }

    public function getFriendlyFire() {
        return $this->friendlyFire;
    }

    public function getName() {
        return $this->name;
    }

}
