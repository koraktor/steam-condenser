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

require_once STEAM_CONDENSER_PATH . 'steam/community/l4d/AbstractL4DStats.php';
require_once STEAM_CONDENSER_PATH . 'steam/community/l4d/L4D2Map.php';
require_once STEAM_CONDENSER_PATH . 'steam/community/l4d/L4D2Weapon.php';
require_once STEAM_CONDENSER_PATH . 'steam/community/l4d/L4DExplosive.php';

/**
 * The L4DStats class represents the game statistics for a single user in
 * Left4Dead
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class L4D2Stats extends AbstractL4DStats {

    protected static $SPECIAL_INFECTED = array('boomer', 'charger', 'hunter', 'jockey', 'smoker', 'spitter', 'tank');

    private $scavengeStats;

    /**
     * Creates a L4D2Stats object by calling the super constructor with the game
     * name "l4d2"
     */
    public function __construct($steamId) {
        parent::__construct($steamId, 'l4d2');
    }

    /**
     * Returns a Hash of lifetime statistics for this user like the time played.
     * If the lifetime statistics haven't been parsed already, parsing is done
     * now.
     *
     * There are only a few additional lifetime statistics for Left4Dead 2
     * which are not generated for Left4Dead, so this calls
     * AbstractL4DStats#getLifetimeStats() first and adds some additional stats.
     */
    public function getLifetimeStats() {
        if(!$this->isPublic()) {
            return;
        }

        if(empty($this->lifetimeStats)) {
            parent::getLifetimeStats();
            $this->lifetimeStats['avgAdrenalineShared']   = (float) $this->xmlData->stats->lifetime->adrenalineshared;
            $this->lifetimeStats['avgAdrenalineUsed']     = (float) $this->xmlData->stats->lifetime->adrenalineused;
            $this->lifetimeStats['avgDefibrillatorsUsed'] = (float) $this->xmlData->stats->lifetime->defibrillatorsused;
        }

        return $this->lifetimeStats;
    }

    /* Returns a Hash of Scavenge statistics for this user like the number of
     * Scavenge rounds played.
     * If the Scavenge statistics haven't been parsed already, parsing is done
     * now.
     */
    public function getScavengeStats() {
        if(!$this->isPublic()) {
            return;
        }

        if(empty($this->scavengeStats)) {
            $this->scavengeStats = array();
            $this->scavengeStats['avgCansPerRound'] = (float) $this->xmlData->stats->scavenge->avgcansperround;
            $this->scavengeStats['perfectRounds']   = (int)   $this->xmlData->stats->scavenge->perfect16canrounds;
            $this->scavengeStats['roundsLost']      = (int)   $this->xmlData->stats->scavenge->roundslost;
            $this->scavengeStats['roundsPlayed']    = (int)   $this->xmlData->stats->scavenge->roundsplayed;
            $this->scavengeStats['roundsWon']       = (int)   $this->xmlData->stats->scavenge->roundswon;
            $this->scavengeStats['totalCans']       = (int)   $this->xmlData->stats->scavenge->totalcans;

            $this->scavengeStats['maps'] = array();
            foreach($this->xmlData->stats->scavenge->mapstats->children() as $mapData) {
                $map_id = (string) $mapData->name;
                $this->scavengeStats['maps'][$map_id] = array();
                $this->scavengeStats['maps'][$map_id]['avgRoundScore']     = (int)    $mapData->avgscoreperround;
                $this->scavengeStats['maps'][$map_id]['highestGameScore']  = (int)    $mapData->highgamescore;
                $this->scavengeStats['maps'][$map_id]['highestRoundScore'] = (int)    $mapData->avgscoreperround;
                $this->scavengeStats['maps'][$map_id]['name']              = (string) $mapData->fullname;
                $this->scavengeStats['maps'][$map_id]['roundsPlayed']      = (int)    $mapData->roundsplayed;
                $this->scavengeStats['maps'][$map_id]['roundsWon']         = (int)    $mapData->roundswon;
            }

            $this->scavengeStats['infected'] = array();
            foreach($this->xmlData->stats->scavenge->infectedstats->children() as $infectedData) {
                $infectedId = (string) $infectedData->name;
                $this->scavengeStats['infected'][$infectedId] = array();
                $this->scavengeStats['infected'][$infectedId]['maxDamagePerLife']    = (int) $infectedData->maxdmg1life;
                $this->scavengeStats['infected'][$infectedId]['maxPoursInterrupted'] = (int) $infectedData->maxpoursinterrupted;
                $this->scavengeStats['infected'][$infectedId]['specialAttacks']      = (int) $infectedData->specialattacks;
            }
        }

        return $this->scavengeStats;
    }

    /**
     * Returns an array of Survival statistics for this user like revived
     * teammates.
     * If the Survival statistics haven't been parsed already, parsing is done
     * now.
     *
     * The XML layout for the Survival statistics for Left4Dead 2 differs a bit
     * from Left4Dead's Survival statistics. So we have to use a different way
     * of parsing for the maps and we use a different map class (L4D2Map) which
     * holds the additional information provided in Left4Dead 2's statistics.
     */
    public function getSurvivalStats() {
        if(!$this->isPublic()) {
            return;
        }

        if(empty($this->survivalStats)) {
            parent::getSurvivalStats();
            $this->survivalStats['maps'] = array();
            foreach($this->xmlData->stats->survival->maps->children() as $mapData) {
                $map = new L4D2Map($mapData);
                $this->survivalStats['maps'][$map->getId()] = $map;
            }
        }

        return $this->survivalStats;
    }

    /**
     * Returns a Hash of L4D2Weapon for this user containing all Left4Dead 2
     * weapons.
     * If the weapons haven't been parsed already, parsing is done now.
     */
    public function getWeaponStats() {
        if(!$this->isPublic()) {
            return;
        }

        if(empty($this->weaponStats)) {
          $this->weaponStats = array();
          foreach($this->xmlData->stats->weapons->children() as $weaponData) {
            if(empty($weaponData)) {
                continue;
            }

            $weaponName = $weaponData->getName();
            if(!in_array($weaponName, array('bilejars', 'molotov', 'pipes'))) {
              $weapon = new L4D2Weapon($weaponData);
            }
            else {
              $weapon = new L4DExplosive($weaponData);
            }

            $this->weaponStats[$weaponName] = $weapon;
          }
        }

        return $this->weaponStats;
    }

    /**
     * Hacky workaround for PHP not allowing arrays as class constants
     */
    protected function SPECIAL_INFECTED() {
        return self::$SPECIAL_INFECTED;
    }

}
?>
