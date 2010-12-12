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

require_once STEAM_CONDENSER_PATH . 'steam/community/alien_swarm/AlienSwarmMission.php';
require_once STEAM_CONDENSER_PATH . 'steam/community/alien_swarm/AlienSwarmWeapon.php';


class AlienSwarmStats extends GameStats {

    const BASE_URL = 'http://steamcommunity.com/public/images/gamestats/swarm/';

    private static $WEAPONS = array('Autogun', 'Cannon_Sentry', 'Chainsaw',
        'Flamer', 'Grenade_Launcher', 'Hand_Grenades', 'Hornet_Barrage',
        'Incendiary_Sentry', 'Laser_Mines', 'Marskman_Rifle', 'Minigun',
        'Mining_Laser', 'PDW', 'Pistol', 'Prototype_Rifle', 'Rail_Rifle',
        'Rifle', 'Rifle_Grenade', 'Sentry_Gun', 'Shotgun', 'Tesla_Cannon',
        'Vindicator', 'Vindicator_Grenade');

    /**
     * @var array
     */
    private $favorites;

    /**
     * @var array
     */
    private $itemStats;

    /**
     * @var array
     */
    private $lifetimeStats;

    /**
     * @var array
     */
    private $missionStats;

    /**
     * @var array
     */
    private $weaponStats;

    public function __construct($steamId) {
        parent::__construct($steamId, 'alienswarm');

        if($this->isPublic()) {
            $lifetimeStats = $this->xmlData->stats->lifetime;
            $this->hoursPlayed = (string) $lifetimeStats->timePlayed;

            $this->lifetimeStats = array();
            $this->lifetimeStats['accuracy']           = (float) $lifetimeStats->accuracy;
            $this->lifetimeStats['aliensBurned']       = (int) $lifetimeStats->aliensburned;
            $this->lifetimeStats['aliensKilled']       = (int) $lifetimeStats->alienskilled;
            $this->lifetimeStats['campaigns']          = (int) $lifetimeStats->campaigns;
            $this->lifetimeStats['damageTaken']        = (int) $lifetimeStats->damagetaken;
            $this->lifetimeStats['experience']         = (int) $lifetimeStats->experience;
            $this->lifetimeStats['experienceRequired'] = (int) $lifetimeStats->xprequired;
            $this->lifetimeStats['fastHacks']          = (int) $lifetimeStats->fasthacks;
            $this->lifetimeStats['friendlyFire']       = (int) $lifetimeStats->friendlyfire;
            $this->lifetimeStats['gamesSuccessful']    = (int) $lifetimeStats->gamessuccess;
            $this->lifetimeStats['healing']            = (int) $lifetimeStats->healing;
            $this->lifetimeStats['killsPerHour']       = (float) $lifetimeStats->killsperhour;
            $this->lifetimeStats['level']              = (int) $lifetimeStats->level;
            $this->lifetimeStats['promotion']          = (int) $lifetimeStats->promotion;
            $this->lifetimeStats['nextUnlock']         = (string) $lifetimeStats->nextunlock;
            $this->lifetimeStats['nextUnlockImg']      = self::BASE_URL . (string) $lifetimeStats->nextunlockimg;
            $this->lifetimeStats['shotsFired']         = (int) $lifetimeStats->shotsfired;
            $this->lifetimeStats['totalGames']         = (int) $lifetimeStats->totalgames;

            if($this->lifetimeStats['promotion'] > 0) {
                $this->lifetimeStats['promotionImg'] = self::BASE_URL . (string) $lifetimeStats->promotionpic;
            }

            $this->lifetimeStats['games_successful_percentage'] = ($this->lifetimeStats['totalGames'] > 0) ? $this->lifetimeStats['gamesSuccessful'] / $this->lifetimeStats['totalGames'] : 0;
        }
    }

    /**
     * Returns a Hash of favorites for this user like weapons and marine. If
     * the favorites haven't been parsed already, parsing is done now.
     *
     * @return array
     */
    public function getFavorites() {
        if(!$this->isPublic()) {
            return;
        }

        if(empty($this->favorites)) {
            $favoritesData = $this->xmlData->stats->favorites;

            $this->favorites = array();
            $this->favorites['class']                      = (string) $favoritesData->class;
            $this->favorites['classImg']                   = (string) $favoritesData->classimg;
            $this->favorites['classPercentage']            = (float) $favoritesData->classpct;
            $this->favorites['difficulty']                 = (string) $favoritesData->difficulty;
            $this->favorites['difficultyPercentage']       = (float) $favoritesData->difficultypct;
            $this->favorites['extra']                      = (string) $favoritesData->extra;
            $this->favorites['extraImg']                   = (string) $favoritesData->extraimg;
            $this->favorites['extraPercentage']            = (float) $favoritesData->extrapct;
            $this->favorites['marine']                     = (string) $favoritesData->marine;
            $this->favorites['marineImg']                  = (string) $favoritesData->marineimg;
            $this->favorites['marinePercentage']           = (float) $favoritesData->marinepct;
            $this->favorites['mission']                    = (string) $favoritesData->mission;
            $this->favorites['missionImg']                 = (string) $favoritesData->missionimg;
            $this->favorites['missionPercentage']          = (float) $favoritesData->missionpct;
            $this->favorites['primaryWeapon']              = (string) $favoritesData->primary;
            $this->favorites['primaryWeaponImg']           = (string) $favoritesData->primaryimg;
            $this->favorites['primaryWeaponPercentage']    = (float) $favoritesData->primarypct;
            $this->favorites['secondaryWeapon']            = (string) $favoritesData->secondary;
            $this->favorites['secondaryWeaponImg']         = (string) $favoritesData->secondaryimg;
            $this->favorites['secondaryWeapon_Percentage'] = (float) $favoritesData->secondarypct;
        }

        return $this->favorites;
    }

    /**
     * Returns an array of item stats for this user like ammo deployed and
     * medkits used. If the items haven't been parsed already, parsing is done
     * now.
     *
     * @return array
     */
    public function getItemStats() {
        if(!$this->isPublic()) {
            return;
        }

        if(empty($this->itemStats)) {
            $itemStatsData = $this->xmlData->stats->weapons;

            $this->itemStats = array();
            $this->itemStats['ammoDeployed']           = (int) $itemStatsData->ammo_deployed;
            $this->itemStats['sentrygunsDeployed']     = (int) $itemStatsData->sentryguns_deployed;
            $this->itemStats['sentryFlamersDeployed']  = (int) $itemStatsData->sentry_flamers_deployed;
            $this->itemStats['sentryFreezeDeployed']   = (int) $itemStatsData->sentry_freeze_deployed;
            $this->itemStats['sentryCannonDeployed']   = (int) $itemStatsData->sentry_cannon_deployed;
            $this->itemStats['medkitsUsed']            = (int) $itemStatsData->medkits_used;
            $this->itemStats['flaresUsed']             = (int) $itemStatsData->flares_used;
            $this->itemStats['adrenalineUsed']         = (int) $itemStatsData->adrenaline_used;
            $this->itemStats['teslaTrapsDeployed']     = (int) $itemStatsData->tesla_traps_deployed;
            $this->itemStats['freezeGrenadesThrown']   = (int) $itemStatsData->freeze_grenades_thrown;
            $this->itemStats['electricArmorUsed']      = (int) $itemStatsData->electric_armor_used;
            $this->itemStats['healgunHeals']           = (int) $itemStatsData->healgun_heals;
            $this->itemStats['healgunHealsSelf']       = (int) $itemStatsData->healgun_heals_self;
            $this->itemStats['healbeaconHeals']        = (int) $itemStatsData->healbeacon_heals;
            $this->itemStats['healbeaconHealsSelf']    = (int) $itemStatsData->healbeacon_heals_self;
            $this->itemStats['damageAmpsUsed']         = (int) $itemStatsData->damage_amps_used;
            $this->itemStats['healbeaconsDeployed']    = (int) $itemStatsData->healbeacons_deployed;
            $this->itemStats['healbeaconHealsPct']     = (float) $itemStatsData->healbeacon_heals_pct;
            $this->itemStats['healgunHealsPct']        = (float) $itemStatsData->healgun_heals_pct;
            $this->itemStats['healbeaconHealsPctSelf'] = (float) $itemStatsData->healbeacon_heals_pct_self;
            $this->itemStats['healgunHealsPctSelf']    = (float) $itemStatsData->healgun_heals_pct_self;
        }

        return $this->itemStats;
    }

    /**
     * Returns an array of AlienSwarmMission for this user containing all Alien
     * Swarm missions. If the missions haven't been parsed already, parsing is
     * done now.
     *
     * @return array
     */
    public function getMissionStats() {
        if(!$this->isPublic()) {
            return;
        }

        if(empty($this->missionStats)) {
            $this->missionStats = array();
            foreach($this->xmlData->stats->missions->children() as $missionData) {
                $this->missionStats[$missionData->getName()] = new AlienSwarmMission($missionData);
            }
        }

        return $this->missionStats;
    }

    /**
     * Returns an array of AlienSwarmWeapon for this user containing all Alien
     * Swarm weapons. If the weapons haven't been parsed already, parsing is
     * done now.
     */
    public function getWeaponStats() {
        if(!$this->isPublic()) {
            return;
        }

        if(empty($this->weaponStats)) {
            $this->weaponStats = array();
            foreach(self::$WEAPONS as $weaponNode) {
                $weaponData = $this->xmlData->stats->weapons->$weaponNode;
                $weapon = new AlienSwarmWeapon($weaponData);
                $this->weaponStats[$weapon->getName()] = $weapon;
            }
        }

        return $this->weaponStats;
    }

}
