<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 *
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 */

require_once STEAM_CONDENSER_PATH . 'steam/community/tf2/TF2Class.php';

/**
 * Represents the stats for the Team Fortress 2 Spy class for a specific user
 *
 * @author     Sebastian Staudt
 * @package    steam-condenser
 * @subpackage community
 */
class TF2Spy extends TF2Class {

    /**
     * @var int
     */
    private $maxBackstabs;

    /**
     * @var int
     */
    private $maxHealthLeeched;

    /**
     * Creates a new instance of the Spy class based on the given XML data
     *
     * @param SimpleXMLElement $classData The XML data for this Spy
     */
    public function __construct($classData) {
        parent::__construct($classData);

        $this->maxBackstabs     = (int) $classData->ibackstabs;
        $this->maxHealthLeeched = (int) $classData->ihealthpointsleached;
    }

    /**
     * Returns the maximum health leeched from enemies by the player in a single
     * life as a Spy
     *
     * @return int Maximum health leeched
     */
    public function getMaxBackstabs() {
        return $this->maxBackstabs;
    }

    /**
     * Returns the maximum health leeched from enemies by the player in a single
     * life as a Spy
     *
     * @return int Maximum health leeched
     */
    public function getMaxHealthLeeched() {
        return $this->maxHealthLeeched;
    }
}
?>
