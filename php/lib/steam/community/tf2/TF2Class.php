<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage Steam Community
 */

require_once "steam/community/GameClass.php";

/**
 * Represents the stats for a Team Fortress 2 class for a specific user
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class TF2Class extends GameClass {
    
    /**
     * Creates a new instance of TF2Class based on the assigned XML data
     * @param $classData
     */
    public function __construct($classData) {
        $this->name                  = (string) $classData->className;
        $this->maxBuildingsDestroyed = (int)    $classData->ibuildingsdestroyed;
        $this->maxCaptures           = (int)    $classData->ipointcaptures;
        $this->maxDamage             = (int)    $classData->idamagedealt;
        $this->maxDefenses           = (int)    $classData->ipointdefenses;
        $this->maxDominations        = (int)    $classData->idominations;
        $this->maxKillAssists        = (int)    $classData->ikillassists;
        $this->maxKils               = (int)    $classData->inumberofkills;
        $this->maxRevenges           = (int)    $classData->irevenge;
        $this->maxScore              = (int)    $classData->ipointsscored;
        $this->maxTimeAlive          = (int)    $classData->iplaytime;
        $this->playTime              = (int)    $classData->playtimeSeconds;
    }
}
?>
