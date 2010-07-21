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

require_once 'steam/community/SteamId.php';
require_once 'steam/community/WebApi.php';

/**
 * Represents the special Team Fortress 2 item Golden Wrench. It includes the
 * ID of the item, the serial number of the wrench, a reference to the SteamId
 * of the owner and the date this player crafted the wrench
 *
 * @package    Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class TF2GoldenWrench {

    /**
     * @var array
     */
    private static $goldenWrenches = null;

    /**
     * @var int
     */
    private $date;

    /**
     * @var int
     */
    private $id;

    /**
     * @var int
     */
    private $number;

    /**
     * @var SteamId
     */
    private $owner;

    /**
     * Returns a Set of all golden wrenches (as instances of TF2GoldenWrench)
     *
     * @return A Set containing all golden wrenches
     * @throws JSONException On JSON errors
     * @throws SteamCondenserException If an error occurs querying the Web API
     *                                 or the Steam Community
     */
    public static function getGoldenWrenches() {
        if(self::$goldenWrenches == null) {
            self::$goldenWrenches = array();

            $data = json_decode(WebApi::getJSON('ITFItems_440', 'GetGoldenWrenches'));
            foreach($data->results->wrenches->wrench as $wrenchData) {
                self::$goldenWrenches[] = new TF2GoldenWrench($wrenchData);
            }
        }

        return self::$goldenWrenches;
    }

    /**
     * Creates a new instance of TF2GoldenWrench with the given data
     *
     * @param stdClass $wrenchData The JSON data for this wrench
     */
    private function __construct($wrenchData) {
        $this->date   = (int) $wrenchData->timestamp;
        $this->id     = (int) $wrenchData->itemID;
        $this->number = (int) $wrenchData->wrenchNumber;
        $this->owner  = SteamId::create((string) $wrenchData->steamID, false);
    }

    /**
     * Returns the date this Golden Wrench has been crafted
     *
     * @return int The crafting date of this wrench
     */
    public function getDate() {
        return $this->date;
    }

    /**
     * Returns the unique item ID of this Golden Wrench
     *
     * @return int The ID of this wrench
     */
    public function getId() {
        return $this->id;
    }

    /**
     * Returns the serial number of this Golden Wrench
     *
     * @return The serial of this wrench
     */
    public function getNumber() {
        return $this->number;
    }

    /**
     * Returns the SteamId of the owner of this Golden Wrench
     *
     * @return SteamId The owner of this wrench
     */
    public function getOwner() {
        return $this->owner;
    }
}
?>
