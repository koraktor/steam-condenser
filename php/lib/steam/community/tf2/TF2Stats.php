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

require_once "steam/community/GameStats.php";
require_once "steam/community/tf2/TF2ClassFactory.php";

/**
 * The TF2Stats class represents the game statistics for a single user in Team
 * Fortress 2
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class TF2Stats extends GameStats {

    /**
     * Creates a TF2Stats object by calling the super constructor with the game
     * name "TF2"
     * @param $steamId
     */
    public function __construct($steamId) {
        parent::__construct($steamId, "TF2");

        if($this->isPublic()) {
            $this->accumulatedPoints = intval($this->xmlData->stats->accumulatedPoints);
        }
    }

    /**
     * Returns a Hash of TF2Class for this user containing all Team Fortress 2
     * classes. If the classes haven't been parsed already, parsing is done now.
     */
    public function getClassStats() {
        if(!$this->isPublic()) {
            return;
        }

        if(empty($this->classStats)) {
            foreach($this->xmlData->stats->classData as $classData) {
                $this->classStats[$classData->className] = TF2ClassFactory::getTF2Class($classData);
            }
        }

        return $this->classStats;
    }
}
?>
