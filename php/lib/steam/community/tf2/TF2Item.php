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

require_once STEAM_CONDENSER_PATH . 'steam/community/GameItem.php';
require_once STEAM_CONDENSER_PATH . 'steam/community/tf2/TF2Inventory.php';

/**
 * Represents a Team Fortress 2 item
 *
 * @package    Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class TF2Item extends GameItem {

    /**
     * @var array
     */
    private static $CLASSES = array('scout', 'sniper', 'soldier', 'demoman',
                                    'medic', 'heavy', 'pyro', 'spy');

    /**
     * @var array
     */
    private $equipped;

    /**
     * Creates a new instance of a TF2Item with the given data
     */
    public function __construct(TF2Inventory $inventory, $itemData) {
        parent::__construct($inventory, $itemData);

        $this->equipped = array();
        foreach(self::$CLASSES as $classId => $className) {
            $this->equipped[$className] = ($itemData->inventory & (1 << 16 + $classId) != 0);
        }
    }

    /**
     * Returns the class symbols for each class this player has equipped this item
     */
    public function getClassesEquipped() {
        $classesEquipped = array();
        foreach($this->equipped as $classId => $equipped) {
            if($equipped) {
                $classesEquipped[] = $classId;
            }
        }

        return $classesEquipped;
    }

    /**
     * Returns whether this item is equipped by this player at all
     */
    public function isEquipped() {
        return in_array(true, $this->equipped);
    }

}
?>
