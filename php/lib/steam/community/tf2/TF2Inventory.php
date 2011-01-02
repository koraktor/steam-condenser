<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2010-2011, Sebastian Staudt
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
     * @var TF2Inventory[]
     */
    private static $cache = array();

    /**
     * @var int
     */
    private $fetchDate;

    /**
     * @var TF2Item[]
     */
    private $items;

    /**
     * @var string
     */
    private $steamId64;

    /**
     * Returns whether the requested inventory is already cached
     *
     * @param string $steamId64 The 64bit Steam ID of the user
     * @return bool Whether the inventory of the given user is already cached
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
     * @param string $steamId64 The 64bit Steam ID of the user
     * @param bool $fetchNow Whether the data should be fetched now
     * @param bool $bypassCache Whether the cache should be bypassed
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
     * @param string $steamId64 The 64bit Steam ID of the user
     * @param bool $fetchNow Whether the data should be fetched now
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
     * Returns the item at the given position in the backpack. The positions
     * range from 1 to 100 instead of the usual array indices (0 to 99).
     *
     * @param int $index The position of the item in the backpack
     * @return TF2Item The item at the given position
     */
    public function getItem($index) {
        return $this->items[$index - 1];
    }

    /**
     * Returns an array of all items in this players inventory.
     *
     * @return TF2Item[] All items in the backpack
     */
    public function getItems() {
        return $this->items;
    }

    /**
     * Returns the 64bit SteamID of the player owning this inventory
     *
     * @return string The 64bit SteamID
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

        $this->fetchDate = time();
    }

    /**
     * Returns whether the items contained in this inventory have been already
     * fetched
     *
     * @return bool Whether the contents backpack have been fetched
     */
    public function isFetched() {
        return !empty($this->fetchDate);
    }

    /**
     * Returns the number of items in the user's backpack
     *
     * @return int The number of items in the backpack
     */
    public function size() {
        return sizeof($this->items);
    }

}
?>
