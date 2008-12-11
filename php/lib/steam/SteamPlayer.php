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

		if(!is_int($score) || $score < 0)
		{
			throw new Exception("Player points have to be a number greater than or equal 0.");
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
	 * @return String
	 */
	public function __toString()
	{
		return "#{$this->id} \"{$this->name}\" {$this->score} {$this->connectTime}";
	}
}
?>