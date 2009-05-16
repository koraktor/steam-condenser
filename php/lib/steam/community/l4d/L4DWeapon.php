<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009, Sebastian Staudt
 *
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */

require_once 'steam/community/GameWeapon.php';

/**
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class L4DWeapon extends GameWeapon {

    private $accuracy;

    private $headshotsPercentage;

    private $killPercentage;

    /**
     * Creates a new instance of L4DWeapon based on the assigned XML data
     * @param $weaponData
     */
    public function __construct($weaponData) {
        parent::__construct($weaponData);

        $this->accuracy            = (string) $weaponData->accuracy;
        $this->headshotsPercentage = (string) $weaponData->headshots;
        $this->id                  = $weaponData->getName();
        $this->killPercentage      = (string) $weaponData->killpct;
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
