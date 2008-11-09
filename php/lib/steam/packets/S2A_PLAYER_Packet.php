<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 * 
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage Packets
 * @version $Id$
 */

require_once "steam/SteamPlayer.php";
require_once "steam/packets/SteamPacket.php";

/**
 * @package Steam Condenser (PHP)
 * @subpackage Packets
 */
class S2A_PLAYER_Packet extends SteamPacket
{
  /**
   * @var SteamPlayer[]
   */
  private $playerArray;

  /**
   * @param mixed $contentData
   */
  public function __construct($contentData)
  {
    if(empty($contentData))
    {
      throw new Exception("Wrong formatted S2A_PLAYER packet.");
    }
    parent::__construct(SteamPacket::S2A_PLAYER_HEADER, $contentData);

    $this->contentData->getByte();
    
    $this->playerArray = array();
    while($this->contentData->remaining() > 0)
    {
      $playerData = array($this->contentData->getByte(), $this->contentData->getString(), $this->contentData->getLong(), $this->contentData->getFloat());
      $this->playerArray[$playerData[0]] = new SteamPlayer($playerData[0], $playerData[1], $playerData[2], $playerData[3]);
    }
  }

  /**
   * @return SteamPlayer[]
   */
  public function getPlayerArray()
  {
    return $this->playerArray;
  }
}
?>
