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

/**
 * AlienSwarmMission holds statistical information about missions played by a
 * player in Alien Swarm.
 */
class AlienSwarmMission {

    private $avgDamageTaken;

    private $avgFriendlyFire;

    private $avgKills;

    private $bestDifficulty;

    private $damageTaken;

    private $friendlyFire;

    private $gamesSuccessful;

    private $img;

    private $kills;

    private $mapName;

    private $name;

    private $time;

    private $totalGames;

    private $totalGamesPercentage;

    /**
     * Creates a new instance of AlienSwarmMission based on the assigned
     * mission name and XML data
     *
     * @param $missionData
     */
    public function __construct($missionData) {
        $this->avgDamageTaken       = (float) $missionData->damagetakenavg;
        $this->avgFriendlyFire      = (float) $missionData->friendlyfireavg;
        $this->avgKills             = (float) $missionData->killsavg;
        $this->bestDifficulty       = (string) $missionData->bestdifficulty;
        $this->damageTaken          = (int) $missionData->damagetaken;
        $this->friendlyFire         = (int) $missionData->friendlyfire;
        $this->gamesSuccessful      = (int) $missionData->gamessuccess;
        $this->img                  = AlienSwarmStats::BASE_URL . (string) $missionData->image;
        $this->kills                = (int) $missionData->kills;
        $this->mapName              = $missionData->getName();
        $this->name                 = (string) $missionData->name;
        $this->totalGames           = (int) $missionData->gamestotal;
        $this->totalGamesPercentage = (float) $missionData->gamestotalpct;

        $this->time = array();
        $this->time['average'] = (string) $missionData->avgtime;
        $this->time['brutal']  = (string) $missionData->brutaltime;
        $this->time['easy']    = (string) $missionData->easytime;
        $this->time['hard']    = (string) $missionData->hardtime;
        $this->time['insane']  = (string) $missionData->insanetime;
        $this->time['normal']  = (string) $missionData->normaltime;
        $this->time['total']   = (string) $missionData->totaltime;
    }

    public function getAvgDamageTaken() {
        return $this->avgDamageTaken;
    }

    public function getAvgFriendlyFire() {
        return $this->avgFriendlyFire;
    }

    public function getAvgKills() {
        return $this->avgKills;
    }

    public function getBestDifficulty() {
        return $this->bestDifficulty;
    }

    public function getDamageTaken() {
        return $this->damageTaken;
    }

    public function getFriendlyFire() {
        return $this->friendlyFire;
    }

    public function getGamesSuccessful() {
        return $this->gamesSuccessful;
    }

    public function getImg() {
        return $this->img;
    }

    public function getKills() {
        return $this->kills;
    }

    public function getMapName() {
        return $this->mapName;
    }

    public function getName() {
        return $this->name;
    }

    public function getTime() {
        return $this->time;
    }

    public function getTotalGames() {
        return $this->totalGames;
    }

    public function getTotalGamesPercentage() {
        return $this->totalGamesPercentage;
    }

}
