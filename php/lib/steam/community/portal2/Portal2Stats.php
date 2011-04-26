<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2011, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage Steam Community
 */

require_once STEAM_CONDENSER_PATH . 'steam/community/GameStats.php';
require_once STEAM_CONDENSER_PATH . 'steam/community/portal2/Portal2Inventory.php';

/**
 * The Portal2Stats class represents the game statistics for a single user in
 * Portal 2
 *
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class Portal2Stats extends GameStats {

    /**
     * @var Portal2Inventory
     */
    private $inventory;

    /**
     * Creates a Portal2Stats object by calling the super constructor with the
     * game name "portal2"
     *
     * @param $steamId The custom URL or 64bit Steam ID of the user
     */
    public function __construct($steamId) {
        parent::__construct($steamId, "portal2");
    }

    /**
     * Returns the current Portal 2 inventory (a.k.a. Robot Enrichment) of this
     * player
     *
     * @return Portal2Inventory This player's Portal 2 backpack
     */
    public function getInventory() {
        if(!$this->isPublic()) {
            return;
        }

        if(empty($this->inventory)) {
            $this->inventory = Portal2Inventory::create($this->steamId64);
        }

        return $this->inventory;
    }
}
?>
