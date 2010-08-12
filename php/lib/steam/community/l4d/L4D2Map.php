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

require_once STEAM_CONDENSER_PATH . 'steam/community/SteamId.php';
require_once STEAM_CONDENSER_PATH . 'steam/community/l4d/L4DMap.php';

/**
 * L4D2Map holds statistical information about maps played by a player in
 * Survival mode of Left4Dead 2. The basic information provided is more or less
 * the same for Left4Dead and Left4Dead 2, but parsing has to be done
 * differently.
 *
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class L4D2Map extends L4DMap{

    private static $INFECTED = array('boomer', 'charger', 'common', 'hunter', 'jockey', 'smoker', 'spitter', 'tank');

    private static $ITEMS = array('adrenaline', 'defibs', 'medkits', 'pills');

    private $items;

    private $kills;

    private $played;

    private $teammates;

    /**
     * Creates a new instance of L4D2Map based on the assigned XML data
     *
     * The map statistics for the Survival mode of Left4Dead 2 hold much more
     * information than those for Left4Dead, e.g. the teammates and items are
     * listed.
     *
     * @param $mapData
     */
    public function __construct($mapData) {
        $this->bestTime    = (float)  $mapData->besttimeseconds;
        preg_match('#http://steamcommunity.com/public/images/gamestats/550/(.*)\.jpg#', (string) $mapData->img, $id);
        $this->id          = $id[1];
        $this->name        = (string) $mapData->name;
        $this->played      = ((int)   $mapData->hasPlayed == 1);

        if($this->played) {
            $this->bestTime = (float) $mapData->besttimemilliseconds / 1000;

            $this->teammates = array();
            foreach($mapData->teammates->children() as $teammate) {
                $this->teammates[] = new SteamId((string) $teammate, false);
            }

            $this->items = array();
            foreach(self::$ITEMS as $item) {
                $this->items[$item] = (int) $mapData->{"item_$item"};
            }

            $this->kills = array();
            foreach(self::$INFECTED as $infected) {
                $this->kills[$infected] = (int) $mapData->{"kills_$infected"};
            }

            switch((string) $mapData->medal) {
                case 'gold':
                    $this->medal = self::GOLD;
                    break;
                case 'silver':
                    $this->medal = self::SILVER;
                    break;
                case 'bronze':
                    $this->medal = self::BRONZE;
                    break;
                default:
                    $this->medal = self::NONE;
            }
        }
    }

    public function getItems() {
        return $this->items;
    }

    public function getKills() {
        return $this->kills;
    }

    public function getTeammates() {
        return $this->teammates;
    }

    public function isPlayed() {
        return $this->played;
    }

}
?>
