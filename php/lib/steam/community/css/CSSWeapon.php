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
 * CSSWeapon holds statistical information about weapons used by a player in
 * Counter-Strike: Source.
 *
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class CSSWeapon {

    private $accuracy;

    private $favorite;

    private $hits;

    private $kills;

    private $ksRatio;

    private $name;

    private $shots;

    /**
     * Creates a new instance of CSSWeapon based on the assigned XML data
     *
     * @param $weaponName
     * @param $weaponsData
     */
    public function __construct($weaponName, $weaponsData) {
        $this->name = $weaponName;

        $this->favorite = ((string) $weaponsData->favorite) == $this->name;
        $this->kills    = (int) $weaponsData->{"{$this->name}_kills"};

        if($this->name != 'grenade' && $this->name != 'knife') {
            $this->hits  = (int) $weaponsData->{"{$this->name}_hits"};
            $this->shots = (int) $weaponsData->{"{$this->name}_shots"};

            $this->accuracy = ($this->shots > 0) ? $this->hits / $this->shots : 0;
            $this->ksRatio  = ($this->shots > 0) ? $this->kills / $this->shots : 0;
        }
    }

    /**
     * Returns whether this weapon is the favorite weapon of this player
     *
     * @return boolean
     */
    public function isFavorite() {
        return $this->favorite;
    }

    public function getAccuracy() {
        return $this->accuracy;
    }

    public function getHits() {
        return $this->hits;
    }

    public function getKills() {
        return $this->kills;
    }

    public function getKsRatio() {
        return $this->ksRatio;
    }

    public function getName() {
        return $this->name;
    }

    public function getShots() {
        return $this->shots;
    }

}
?>
