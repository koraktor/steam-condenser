<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 *
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage SteamPlayer
 */

require_once 'exceptions/SteamCondenserException.php';

/**
 * @package Steam Condenser (PHP)
 * @subpackage SteamPlayer
 */
class SteamPlayer
{
    /**
     * @var float
     */
    private $connectTime;

    /**
     * @var int
     */
    private $id;

    /**
     * @var int
     */
    private $loss;

   /**
    * @var String
    */
    private $name;

    /**
     * @var int
     */
    private $ping;

    /**
     * @var int
     */
    private $score;

    /**
     * @var String
     */
    private $state;

    /**
     * @var String
     */
    private $steamId;

    /**
     * Creates a new SteamPlayer object with the given information
     * @param int $playerId
     * @param String $playerName
     * @param int $playerPoints
     * @param float $playerConnectTime
     */
    public function __construct($id, $name, $score, $connectTime)
    {
        if(!is_int($id) || $id < 0)
        {
            throw new Exception("Player ID has to be a number greater than or equal 0.");
        }

        if(!is_string($name))
        {
            throw new Exception("Player name has to be a string.");
        }

        if(!is_int($score))
        {
            throw new Exception("Player points have to be a number.");
        }

        if(!is_float($connectTime))
        {
            throw new Exception("Player connection time has to be a floating-point integer.");
        }

        $this->connectTime = $connectTime;
        $this->id = $id;
        $this->name = $name;
        $this->score = $score;
    }

    public function addInformation($playerData) {
        $this->extended = true;

        $this->realId  = $playerData[0];
        $this->steamId = $playerData[2];

        if($playerData[1] != $this->name) {
            throw new SteamCondenserException('Information to add belongs to a different player.');
        }

        if($this->steamId == 'BOT') {
            $this->state = $playerData[3];
        }
        else {
            $this->address = $playerData[6];
            $this->loss    = $playerData[4];
            $this->ping    = $playerData[3];
            $this->state   = $playerData[5];
        }
    }

    /**
     * Returns the time this player is connected to the server
     * @return float
     */
    public function getConnectTime()
    {
        return $this->connectTime;
    }

    /**
     * Returns the ID of this player
     * @return int
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * Returns the nickname of this player
     * @return String
     */
    public function getName()
    {
        return $this->name;
    }

    /**
     * Returns the score of this player
     * @return int
     */
    public function getScore()
    {
        return $this->score;
    }

    /**
     * Returns the SteamID of this player
     * @return String
     */
    public function getSteamId()
    {
        return $this->steamId;
    }

    /**
     * Returns a String representation of this player
     * @return String
     */
    public function __toString()
    {
        if($this->extended) {
            return "#{$this->realId} \"{$this->name}\", SteamID: {$this->steamID} Score: {$this->score}, Time: {$this->connectTime}";
        }
        else {
            return "#{$this->id} \"{$this->name}\", Score: {$this->score}, Time: {$this->connectTime}";
        }
    }
}
?>