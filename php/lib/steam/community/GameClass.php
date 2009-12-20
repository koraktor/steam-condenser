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

/**
 * Super class for classes representing player classes
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
abstract class GameClass {

    /**
     * @var String
     */
    protected $name;

    /**
     * @var float
     */
    protected $playtime;


    /**
     * Returns the name of this class
     * @return String
     */
    public function getName() {
        return $this->name;
    }

    /**
     * Returns the time the player has played as this class
     * @return float
     */
    public function getPlayTime() {
        return $this->playtime;
    }

}
?>
