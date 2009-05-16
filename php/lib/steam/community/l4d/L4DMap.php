<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009, Sebastian Staudt
 *
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */

/**
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class L4DMap {

    const GOLD   = 1;

    const SILVER = 2;

    const BRONZE = 3;

    const NONE   = 0;

    private $bestTime;

    private $id;

    private $medal;
    
    private $name;

    private $timesPlayed;

    /**
     * Creates a new instance of L4DMap based on the assigned XML data
     * @param $mapData
     */
    public function __construct($mapData) {
        $this->bestTime    = (float)  $mapData->besttimeseconds;
        $this->id          = $mapData->getName();
        $this->name        = (string) $mapData->name;
        $this->timesPlayed = (int)    $mapData->timesplayed;

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
?>
