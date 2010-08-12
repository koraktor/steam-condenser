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
 * Represents the stats for a Day of Defeat: Source weapon for a specific user
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class DoDSWeapon extends GameWeapon {

    private $avgHitsPerKill;

    private $headshotPercentage;

    private $headshots;

    private $hits;

    private $hitPercentage;

    private $name;

    public function __construct($weaponData) {
        parent::__construct($weaponData);

        $this->headshots = (int)    $weaponData->headshots;
        $this->id        = (string) $weaponData['key'];
        $this->name      = (string) $weaponData->name;
        $this->shots     = (int)    $weaponData->shotsfired;
        $this->hits      = (int)    $weaponData->shotshit;
    }

    /**
     * Returns the average number of hits needed for a kill with this weapon.
     * Calculates the value if needed.
     * @return float
     */
    public function getAvgHitsPerKill() {
        if(empty($this->avgHitsPerKill)) {
            $this->avgHitsPerKill = $this->hits / $this->kills;
        }

        return $this->avgHitsPerKill;
    }

    /**
     * Returns the percentage of headshots relative to the shots hit with this
     * weapon. Calculates the value if needed.
     * @return float
     */
    public function getHeadshotPercentage() {
        if(empty($this->headshotPercentage)) {
            $this->headshotPercentage = $this->headshots / $this->hits;
        }

        return $this->headshotPercentage;
    }

    /**
     * @return int
     */
    public function getHeadshots() {
        return $this->headshots;
    }

    /**
     * Returns the percentage of hits relative to the shots fired with this
     * weapon. Calculates the value if needed.
     * @return float
     */
    public function getHitPercentage() {
        if(empty($this->hitPercentage)) {
            $this->hitPercentage = $this->hits / $this->shots;
        }

        return $this->hitPercentage;
    }

    /**
     * @return String
     */
    public function getName() {
        return $this->name;
    }

    /**
     * @return int
     */
    public function getHits() {
        return $this->hits;
    }

}
?>
