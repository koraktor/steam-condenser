<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 *
 * @author     Sebastian Staudt
 * @license    http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package    Steam Condenser (PHP)
 * @subpackage SteamPlayer
 */

require_once STEAM_CONDENSER_PATH . 'exceptions/SteamCondenserException.php';

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

    /**
     * Extends a player object with information retrieved from a RCON call to
     * the status command
     *
     * @param string playerData The player data retrieved from
     *        <code>rcon status</code>
     * @throws SteamCondenserException
     */
    public function addInformation($playerData) {
        if($playerData['name'] != $this->name) {
            throw new SteamCondenserException('Information to add belongs to a different player.');
        }

        $this->extended = true;
        $this->realId   = intval($playerData['userid']);
        if(array_key_exists('state', $playerData)) {
            $this->state    = $playerData['state'];
        }
        $this->steamId  = $playerData['uniqueid'];

        if(!$this->isBot()) {
            $this->loss = intval($playerData['loss']);
            $this->ping = intval($playerData['ping']);

            if(array_key_exists('data', $playerData)) {
                $address = explode(':', $playerData['adr']);
                $this->ipAddress  = $address[0];
                $this->clientPort = intval($address[1]);
            }

            if(array_key_exists('rate', $playerData)) {
                $this->rate = $playerData['rate'];
            }
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
     * @return Returns the rate of this player
     */
    public function getRate() {
        return $this->rate;
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
     * Returns whether this player object has extended information
     * @return boolean
     */
    public function isBot() {
        return $this->steamId == 'BOT';
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
