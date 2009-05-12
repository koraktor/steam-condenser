<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage SteamPlayer
 * @version $Id$
 */

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
    * @var String
    */
    private $name;

    /**
     * @var int
     */
    private $score;

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
     * Returns a String representation of this player
     * @return String
     */
    public function __toString()
    {
        return "#{$this->id} \"{$this->name}\", Score: {$this->score}, Time: {$this->connectTime}";
    }
}
?>