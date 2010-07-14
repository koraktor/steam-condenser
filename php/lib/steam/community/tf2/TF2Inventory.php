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

require_once STEAM_CONDENSER_PATH . 'steam/community/WebApi.php';
require_once STEAM_CONDENSER_PATH . 'steam/community/tf2/TF2Item.php';

/**
 * Represents the inventory (aka. Backpack) of a Team Fortress 2 player
 *
 * @package    Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class TF2Inventory {

    /**
     * @var array
     */
    private static $cache = array();

    /**
     * @var array
     */
    private $items;

    /**
     * @var String
     */
    private $steamId64;

    /**
     * Returns whether the requested inventory is already cached
     *
     * @param String steamId64
     */
    public static function isCached($steamId64) {
        return array_key_exists($steamId64, self::$cache);
    }

    /**
     * Clears the inventory cache
     */
    public static function clearCache() {
        self::$cache = array();
    }

    /**
     * This checks the cache for an existing inventory. If it exists it is
     * returned. Otherwise a new inventory is created.
     *
     * @param String $steamId64
     * @param boolean $fetchNow
     * @param boolean $bypassCache
     */
    public static function create($steamId64, $fetchNow = true, $bypassCache = false) {
        if(self::isCached($steamId64) && !$bypassCache) {
            $inventory = self::$cache[$steamId64];
            if($fetchNow && !$inventory->isFetched()) {
                $inventory->fetch();
            }
            return $inventory;
        } else {
            return new TF2Inventory($steamId64, $fetchNow);
        }
    }

    /**
     * Creates a new inventory object for the given SteamID64. This calls
     * fetch() to update the data and create the TF2Item instances contained in
     * this players backpack
     *
     * @param String $steamId64
     * @param boolean $fetchNow
     */
    public function __construct($steamId64, $fetchNow = true) {
        $this->steamId64 = $steamId64;

        if($fetchNow) {
            $this->fetch();
        }

        $this->cache();
    }

    /**
     * Saves this inventory in the cache
     */
    public function cache() {
        if(!array_key_exists($this->steamId64, self::$cache)) {
            self::$cache[$this->steamId64] = $this;
        }
    }

    /**
     * Returns the item at the given position in the backpack. The positions range
     * from 1 to 100 instead of the usual array indices (0 to 99).
     */
    public function getItem($index) {
        return $this->items[$index - 1];
    }

    /**
     * Returns an array of all items in this players inventory.
     */
    public function getItems() {
        return $this->items;
    }

    /**
     * Returns the 64bit SteamID of the player owning this inventory
     */
    public function getSteamId64() {
        return $this->steamId64;
    }

    /**
     * Updates the contents of the backpack using Steam Web API
     */
    public function fetch() {
        $result = WebApi::getJSONData('ITFItems_440', 'GetPlayerItems', 1, array('SteamID' => $this->steamId64));

        $this->items = array();
        foreach($result->items->item as $itemData) {
            if($itemData != null) {
                $item = new TF2Item($itemData);
                $this->items[$item->getBackpackPosition() - 1] = $item;
            }
        }
    }

    /**
     * Returns the number of items in the user's backpack
     */
    public function size() {
        return sizeof($this->items);
    }

}
?>
