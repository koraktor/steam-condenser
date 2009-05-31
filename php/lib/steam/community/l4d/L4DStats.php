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

require_once 'steam/community/GameStats.php';
require_once 'steam/community/l4d/L4DExplosive.php';
require_once 'steam/community/l4d/L4DMap.php';
require_once 'steam/community/l4d/L4DWeapon.php';

/**
 * The L4DStats class represents the game statistics for a single user in
 * Left4Dead
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class L4DStats extends GameStats {

    private $favorites;

    private $lifetimeStats;

    private $mostRecentGame;
    
    private $survivalStats;

    private $teamplayStats;

    private $versusStats;

    private $weaponStats;

    /**
     * Creates a L4DStats object by calling the super constructor with the game
     * name "L4D"
     * @param $steamId
     */
    public function __construct($steamId) {
        parent::__construct($steamId, 'L4D');

        if($this->isPublic()) {
            $this->mostRecentGame['difficulty'] = (string) $this->xmlData->stats->mostrecentgame->difficulty;
            $this->mostRecentGame['escaped']    = (bool)   $this->xmlData->stats->mostrecentgame->bEscaped;
            $this->mostRecentGame['movie']      = (string) $this->xmlData->stats->mostrecentgame->movie;
            $this->mostRecentGame['timePlayed'] = (string) $this->xmlData->stats->mostrecentgame->time;
        }
    }

    public function getFavorites() {
        if(!$this->isPublic()) {
            return;
        }

        if(empty($this->favorites)) {
            $this->favorites = array();
            $this->favorites['campaign']                = (string) $this->xmlData->stats->favorites->campaign;
            $this->favorites['campaignPercentage']      = (int)    $this->xmlData->stats->favorites->campaignpct;
            $this->favorites['character']               = (string) $this->xmlData->stats->favorites->character;
            $this->favorites['characterPercentage']     = (int)    $this->xmlData->stats->favorites->characterpct;
            $this->favorites['level1Weapon']            = (string) $this->xmlData->stats->favorites->weapon1;
            $this->favorites['level1Weapon1Percentage'] = (int)    $this->xmlData->stats->favorites->weapon1pct;
            $this->favorites['level2Weapon']            = (string) $this->xmlData->stats->favorites->weapon2;
            $this->favorites['level2Weapon1Percentage'] = (int)    $this->xmlData->stats->favorites->weapon2pct;
        }

        return $this->favorites;
    }

    public function getLifetimeStats() {
        if(!$this->isPublic()) {
            return;
        }

        if(empty($this->lifetimeStats)) {
            $this->lifetimeStats = array();
            $this->lifetimeStats['finalesSurvived']           = (int)    $this->xmlData->stats->lifetime->finales;
            $this->lifetimeStats['gamesPlayed']               = (int)    $this->xmlData->stats->lifetime->gamesplayed;
            $this->lifetimeStats['finalesSurvivedPercentage'] = $this->lifetimeStats['finalesSurvived'] / $this->lifetimeStats['gamesPlayed'];
            $this->lifetimeStats['infectedKilled']            = (int)    $this->xmlData->stats->lifetime->infectedkilled;
            $this->lifetimeStats['killsPerHour']              = (float)  $this->xmlData->stats->lifetime->killsperhour;
            $this->lifetimeStats['avgKitsShared']             = (float)  $this->xmlData->stats->lifetime->kitsshared;
            $this->lifetimeStats['avgKitsUsed']               = (float)  $this->xmlData->stats->lifetime->kitsused;
            $this->lifetimeStats['avgPillsShared']            = (float)  $this->xmlData->stats->lifetime->pillsshared;
            $this->lifetimeStats['avgPillsUsed']              = (float)  $this->xmlData->stats->lifetime->pillused;
            $this->lifetimeStats['timePlayed']                = (string) $this->xmlData->stats->lifetime->timeplayed;
        }

        return $this->lifetimeStats;
    }

    public function getSurvivalStats() {
        if(!$this->isPublic()) {
            return;
        }

        if(empty($this->survivalStats)) {
          $this->survivalStats = array();
          $this->survivalStats['goldMedals']   = (int)   $this->xmlData->stats->survival->goldmedals;
          $this->survivalStats['silverMedals'] = (int)   $this->xmlData->stats->survival->silvermedals;
          $this->survivalStats['bronzeMedals'] = (int)   $this->xmlData->stats->survival->bronzemedals;
          $this->survivalStats['roundsPlayed'] = (int)   $this->xmlData->stats->survival->roundsplayed;
          $this->survivalStats['bestTime']     = (float) $this->xmlData->stats->survival->besttime;

          $this->survivalStats['maps'] = array();
          foreach($this->xmlData->stats->survival->maps->children() as $mapData) {
            $this->survivalStats['maps'][$mapData->getName()] = new L4DMap($mapData);
          }
        }

        return $this->survivalStats;
    }

    public function getTeamplayStats() {
        if(!$this->isPublic()) {
            return;
        }
        
        if(empty($this->teamplayStats)) {
          $this->teamplayStats = array();
          $this->teamplayStats['revived']                    = (int)    $this->xmlData->stats->teamplay->revived;
          $this->teamplayStats['mostRevivedDifficulty']      = (string) $this->xmlData->stats->teamplay->reviveddiff;
          $this->teamplayStats['avgRevived']                 = (float)  $this->xmlData->stats->teamplay->revivedavg;
          $this->teamplayStats['avgWasRevived']              = (float)  $this->xmlData->stats->teamplay->wasrevivedavg;
          $this->teamplayStats['protected']                  = (int)    $this->xmlData->stats->teamplay->protected;
          $this->teamplayStats['mostProtectedDifficulty']    = (string) $this->xmlData->stats->teamplay->protecteddiff;
          $this->teamplayStats['avgProtected']               = (float)  $this->xmlData->stats->teamplay->protectedavg;
          $this->teamplayStats['avgWasProtected']            = (float)  $this->xmlData->stats->teamplay->wasprotectedavg;
          $this->teamplayStats['friendlyFireDamage']         = (int)    $this->xmlData->stats->teamplay->ffdamage;
          $this->teamplayStats['mostFriendlyFireDifficulty'] = (string) $this->xmlData->stats->teamplay->ffdamagediff;
          $this->teamplayStats['avgFriendlyFireDamage']      = (float)  $this->xmlData->stats->teamplay->ffdamageavg;
        }

        return $this->teamplayStats;
    }
    
    public function getVersusStats() {
        if(!$this->isPublic()) {
            return;
        }

        if(empty($this->versusStats)) {
          $this->versusStats = array();
          $this->versusStats['gamesPlayed']               = (int)    $this->xmlData->stats->versus->gamesplayed;
          $this->versusStats['gamesCompleted']            = (int)    $this->xmlData->stats->versus->gamescompleted;
          $this->versusStats['finalesSurvived']           = (int)    $this->xmlData->stats->versus->finales;
          $this->versusStats['finalesSurvivedPercentage'] = ($this->versusStats['games_played']) ? $this->versusStats['finales_survived'] / $this->versusStats['games_played'] : 0;
          $this->versusStats['points']                    = (int)    $this->xmlData->stats->versus->points;
          $this->versusStats['mostPointsInfected']        = (string) $this->xmlData->stats->versus->pointas;
          $this->versusStats['gamesWon']                  = (int)    $this->xmlData->stats->versus->gameswon;
          $this->versusStats['gamesLost']                 = (int)    $this->xmlData->stats->versus->gameslost;
          $this->versusStats['highestSurvivorScore']      = (int)    $this->xmlData->stats->versus->survivorscore;

          foreach(array('boomer', 'hunter', 'smoker', 'tank') as $infected) {
            $this->versusStats[$infected] = array();
            $this->versusStats[$infected]['special']     = (int)   $this->xmlData->stats->versus->{$infected . 'special'};
            $this->versusStats[$infected]['mostDamage']  = (int)   $this->xmlData->stats->versus->{$infected . 'dmg'};
            $this->versusStats[$infected]['avgLifespan'] = (float) $this->xmlData->stats->versus->{$infected . 'lifespan'};
          }
        }

        return $this->versusStats;
    }

    public function getWeaponStats() {
        if(!$this->isPublic()) {
            return;
        }

        if(empty($this->weaponStats)) {
          $this->weaponStats = array();
          foreach($this->xmlData->stats->weapons->children() as $weaponData) {
            $weaponName = $weaponData->getName();
            if($weaponName != 'molotov' && $weaponName != 'pipes') {
              $weapon = new L4DWeapon($weaponData);
            }
            else {
              $weapon = new L4DExplosive($weaponData);
            }

            $this->weaponStats[$weaponName] = $weapon;
          }
        }

        return $this->weaponStats;
    }
}
?>
