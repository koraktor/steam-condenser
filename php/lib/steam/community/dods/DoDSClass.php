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

require_once STEAM_CONDENSER_PATH . 'steam/community/GameClass.php';

/**
 * Represents the stats for a Day of Defeat: Source class for a specific user
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class DoDSClass extends GameClass {

    private $blocks;

    private $bombsDefused;

    private $bombsPlanted;

    private $captures;

    private $deaths;

    private $dominations;

    private $key;

    private $kills;

    private $roundsLost;

    private $roundsWon;

    private $revenges;

    /**
     * Creates a new instance of DoDSClass based on the assigned XML data
     * @param $classData
     */
    public function __construct($classData) {
        $this->blocks       = (int)    $classData->blocks;
        $this->bombsDefused = (int)    $classData->bombsdefused;
        $this->bombsPlanted = (int)    $classData->bombsplanted;
        $this->captures     = (int)    $classData->captures;
        $this->deaths       = (int)    $classData->deaths;
        $this->dominations  = (int)    $classData->dominations;
        $this->key          = (string) $classData['key'];
        $this->kills        = (int)    $classData->kills;
        $this->name         = (string) $classData->name;
        $this->playTime     = (int)    $classData->playtime;
        $this->roundsLost   = (int)    $classData->roundslost;
        $this->roundsWon    = (int)    $classData->roundswon;
        $this->revenges     = (int)    $classData->revenges;
    }

    public function getBlocks() {
        return $this->blocks;
    }

    public function getBombsDefuse() {
        return $this->bombsDefused;
    }

    public function getBombsPlanted() {
        return $this->bombsPlanted;
    }

    public function getCaptures() {
        return $this->captures;
    }

    public function getDeaths() {
        return $this->deaths;
    }

    public function getDominations() {
        return $this->dominations;
    }

    public function getKey() {
        return $this->key;
    }

    public function getKills() {
        return $this->kills;
    }

    public function getRoundsLost() {
        return $this->roundsLost;
    }

    public function getRoundsWon() {
        return $this->roundsWon;
    }

    public function getRevenges() {
        return $this->revenges;
    }
}
?>
