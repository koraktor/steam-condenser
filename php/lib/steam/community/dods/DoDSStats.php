<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 * @version $Id$
 */

require_once 'steam/community/GameStats.php';
require_once 'steam/community/dods/DoDSClass.php';
require_once 'steam/community/dods/DoDSWeapon.php';

/**
 * The DoDSStats class represents the game statistics for a single user in Day
 * of Defeat: Source
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class DoDSStats extends GameStats {

    private $classStats;

    private $weaponStats;

    /**
     * Creates a DoDSStats object by calling the super constructor with the game
     * name "DoD:S"
     * @param $steamId
     */
    public function __construct($steamId) {
        parent::__construct($steamId, 'DoD:S');

        if($this->privacyState == 'public') {
        }
    }

    /**
     * Returns an associative array of DoDSClass for this user containing all
     * Day of Defeat: Source classes. If the classes haven't been parsed
     * already, parsing is done now.
     * @return DoDSClass[]
     */
    public function getClassStats() {
        if(empty($this->classStats)) {
            $this->classStats = array();
            foreach($this->xmlData->stats->classes->children() as $classData) {
                $this->classStats[(string) $classData['key']] = new DoDSClass($classData);
            }
        }

        return $this->classStats;
    }

    /**
     * Returns an associative array of DoDSWeapon for this user containing all
     * DoD:S weapons. If the weapons haven't been parsed already, parsing is
     * done now.
     * @return DoDSWeapon[]
     */
    public function getWeaponStats() {
        if(empty($this->weaponStats)) {
            foreach($this->xmlData->stats->weapons->children() as $classData) {
                $this->weaponStats[(string) $classData['key']] = new DoDSWeapon($classData);
            }
        }

        return $this->weaponStats;
    }
}
?>
