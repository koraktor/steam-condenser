<?php
/**
 * This code is free software; you can redistribute it and/or modify it under the
 * terms of the new BSD License.
 *
 * Copyright (c) 2009, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage Steam Community
 */

require_once STEAM_CONDENSER_PATH . 'steam/community/GameStats.php';

/**
 * The DefenseGridStats class represents the game statistics for a single user
 * in Defense Grid: The Awakening
 *
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class DefenseGridStats extends GameStats {
    /**
     * @var array
     */
    public $towerStats;
    /**
     * @var array
     */
    public $alienStats;

    /**
     * @var int
     */
    public $goldMedals;

    /**
     *
     * @var int
     */
    public $silverMedals;

    /**
     *
     * @var int
     */
    public $bronzeMedals;

    /**
     * Creates a DefenseGridStats object by calling the super constructor with
     * the game name "defensegrid:awakening"
     *
     * @param String $steamId
     */
    public function __construct($steamId) {
        parent::__construct($steamId, 'defensegrid:awakening');

        if($this->isPublic()) {
            $generalData = $this->xmlData->stats->general;

            $this->bronzeMedals          = (int) $generalData->bronze_medals_won->value;
            $this->silverMedals          = (int) $generalData->silver_medals_won->value;
            $this->goldMedals            = (int) $generalData->gold_medals_won->value;
            $this->levelsPlayed          = (int) $generalData->levels_played_total->value;
            $this->levelsPlayedCampaign  = (int) $generalData->levels_played_campaign->value;
            $this->levelsPlayedChallenge = (int) $generalData->levels_played_challenge->value;
            $this->levelsWon             = (int) $generalData->levels_won_total->value;
            $this->levelsWonCampaign     = (int) $generalData->levels_won_campaign->value;
            $this->levelsWonChallenge    = (int) $generalData->levels_won_challenge->value;
            $this->encountered           = (int) $generalData->total_aliens_encountered->value;
            $this->killed                = (int) $generalData->total_aliens_killed->value;
            $this->killedCampaign        = (int) $generalData->total_aliens_killed_campaign->value;
            $this->killedChallenge       = (int) $generalData->total_aliens_killed_challenge->value;
            $this->resources             = (int) $generalData->resources_recovered->value;
            $this->heatDamage            = (float) $generalData->heatdamage->value;
            $this->timePlayed            = (float) $generalData->time_played->value;
            $this->interest              = (float) $generalData->interest_gained->value;
            $this->damage                = (float) $generalData->tower_damage_total->value;
            $this->damageCampaign        = (float) $generalData->tower_damage_total_campaign->value;
            $this->damageChallenge       = (float) $generalData->tower_damage_total_challenge->value;
            $this->orbitalLaserFired     = (int) $this->xmlData->stats->orbitallaser->fired->value;
            $this->orbitalLaserDamage    = (float) $this->xmlData->stats->orbitallaser->damage->value;
        }
    }

    /**
     * Returns stats about the towers built
     *
     * The array returned uses the names of the aliens as keys. Every value of
     * the array is an array containing the number of aliens encountered as the
     * first element and the number of aliens killed as the second element.
     */
    public function getAlienStats() {
        if(!$this->isPublic()) {
            return;
        }

        if(empty($this->alienStats)) {
            $alienData = $this->xmlData->stats->aliens;
            $this->alienStats = array();
            $aliens = array('bulwark', 'crasher', 'dart', 'decoy', 'drone',
                'grunt', 'juggernaut', 'manta', 'racer', 'rumbler', 'seeker',
                'spire', 'stealth', 'swarmer', 'turtle', 'walker');

            foreach($aliens as $alien) {
                $this->alienStats[$alien] = array(
                    (int) $alienData->$alien->encountered->value,
                    (int) $alienData->$alien->killed->value
                );
            }
        }

        return $this->alienStats;
    }

    /**
     * Returns stats about the towers built
     *
     * The Hash returned uses the names of the towers as keys. Every value of
     * the Hash is another Hash using the keys 1 to 3 for different tower
     * levels.
     * The values of these Hash is an Array containing the number of towers
     * built as the first element and the damage dealt by this specific tower
     * type as the second element.
     *
     * The Command tower uses the resources gained as second element.
     * The Temporal tower doesn't have a second element.
     *
     * @return array
     */
    public function getTowerStats() {
        if(!$this->isPublic()) {
            return;
        }

        if(empty($this->towerStats)) {
            $towerData = $this->xmlData->stats->towers;
            $this->towerStats = array();
            $towers = array('cannon', 'flak', 'gun', 'inferno', 'laser',
                'meteor', 'missile', 'tesla');

            foreach($towers as $tower) {
                $this->towerStats[$tower] = array();
                for($i = 1; $i <= 3; $i++) {
                    $built = $towerData->xpath("{$tower}[@level=$i]/built/value");
                    $damage = $towerData->xpath("{$tower}[@level=$i]/damage/value");
                    $this->towerStats[$tower][$i] = array(
                        (int) $built[0],
                        (float) $damage[0]
                    );
                }
            }

            $this->towerStats['command'] = array();
            for($i = 1; $i <= 3; $i++) {
                $built = $towerData->xpath("command[@level=$i]/built/value");
                $resources = $towerData->xpath("command[@level=$i]/resource/value");
                $this->towerStats['command'][$i] = array(
                    (int) $built[0],
                    (float) $resources[0]
                );
            }

            $this->towerStats['temporal'] = array();
            for($i = 1; $i <= 3; $i++) {
                $built = $towerData->xpath("temporal[@level=$i]/built/value");
                $this->towerStats['temporal'][$i] = array(
                    (int) $built[0]
                );
            }
        }

        return $this->towerStats;
    }
}
?>
