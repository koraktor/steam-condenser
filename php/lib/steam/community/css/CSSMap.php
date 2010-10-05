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
 * CSSMap holds statistical information about maps played by a player in
 * Counter-Strike: Source.
 *
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class CSSMap {

    private $favorite;

    private $name;

    private $roundsPlayed;

    private $roundsLost;

    private $roundsWon;

    private $roundsWonPercentage;

    /**
     * Creates a new instance of CSSMap based on the assigned map name and XML
     * data
     *
     * @param $mapName
     * @param $mapsData
     */
    public function __construct($mapName, $mapsData) {
        $this->name = $mapName;

        $this->favorite     = ((string) $mapsData->favorite) == $this->name;
        $this->roundsPlayed = (int) $mapsData->{"{$this->name}_rounds"};
        $this->roundsWon    = (int) $mapsData->{"{$this->name}_wins"};

        $this->roundsLost          = $this->roundsPlayed - $this->roundsWon;
        $this->roundsWonPercentage = ($this->roundsPlayed > 0) ? ($this->roundsWon / $this->roundsPlayed) : 0;
    }

    /**
     * Returns whether this map is the favorite map of this player
     *
     * @return boolean
     */
    public function isFavorite() {
        return $this->favorite;
    }

    public function getName() {
        return $this->name;
    }

    public function getRoundsPlayed() {
        return $this->roundsPlayed;
    }

    public function getRoundsLost() {
        return $this->roundsLost;
    }

    public function getRoundsWon() {
        return $this->roundsWon;
    }

    public function getRoundsWonPercentage() {
        return $this->roundsWonPercentage;
    }
}
?>
