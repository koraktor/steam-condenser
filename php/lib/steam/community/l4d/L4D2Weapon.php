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

require_once 'steam/community/l4d/AbstractL4DWeapon.php';

/**
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class L4D2Weapon extends AbstractL4DWeapon {

    private $damage;

    private $weaponGroup;

    /**
     * Creates a new instance of L4D2Weapon based on the assigned XML data
     * @param $weaponData
     */
    public function __construct($weaponData) {
        parent::__construct($weaponData);

        $this->damage         = (int)    $weaponData->damage;
        $this->killPercentage = (string) $weaponData->pctkills;
        $this->weaponGroup    = $weaponGroup['group'];

    }

    public function getDamage() {
        return $this->damage;
    }

    public function getWeaponGroup() {
        return $this->weaponGroup;
    }
}
?>
