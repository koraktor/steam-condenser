<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
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
     * @var int
     */
    private $clientPort;

    /**
     * @var float
     */
    private $connectTime;

    /**
     * @var boolean
     */
    private $extended;

    /**
     * @var int
     */
    private $id;

    /**
     * @var String
     */
    private $ipAddress;

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
    private $realId;

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
        $this->extended = false;
    }

    public function addInformation($playerData) {
        $this->extended = true;

        $this->realId  = intval($playerData[0]);
        $this->steamId = $playerData[2];

        if($playerData[1] != $this->name) {
            throw new SteamCondenserException('Information to add belongs to a different player.');
        }

        if($this->steamId == 'BOT') {
            $this->state = $playerData[3];
        }
        else {
            $address = explode(':', $playerData[6]);
            $this->ipAddress  = $address[0];
            $this->clientPort = intval($address[1]);
            $this->loss       = intval($playerData[4]);
            $this->ping       = intval($playerData[3]);
            $this->state      = $playerData[5];
        }
    }

    /**
     * Returns the client port of this player
     * @return int
     */
    public function getClientPort()
    {
        return $this->clientPort;
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
     * Returns the IP address of this player
     * @return String
     */
    public function getIpAddress()
    {
        return $this->ipAddress;
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
     * Returns the ping of this player
     * @return int
     */
    public function getPing()
    {
        return $this->ping;
    }

    /**
     * Returns the real ID (as used on the server) of this player
     * @return int
     */
    public function getRealId()
    {
        return $this->realId;
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
     * Returns the connection state of this player
     * @return String
     */
    public function getState()
    {
        return $this->state;
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
     * Returns whether this player object has extended information gathered
     * using RCON
     * @return boolean
     */
    public function isExtended()
    {
        return $this->extended;
    }

    /**
     * Returns a String representation of this player
     * @return String
     */
    public function __toString()
    {
        if($this->extended) {
            return "#{$this->realId} \"{$this->name}\", SteamID: {$this->steamId} Score: {$this->score}, Time: {$this->connectTime}";
        }
        else {
            return "#{$this->id} \"{$this->name}\", Score: {$this->score}, Time: {$this->connectTime}";
        }
    }
}
?>
