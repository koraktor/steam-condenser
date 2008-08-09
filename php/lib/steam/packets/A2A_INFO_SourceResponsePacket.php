<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage A2A_INFO_SourceResponsePacket
 * @version $Id$
 */

require_once "steam/packets/A2A_INFO_ResponsePacket.php";

/**
 * @package Steam Condenser (PHP)
 * @subpackage A2A_INFO_SourceResponsePacket
 */
class A2A_INFO_SourceResponsePacket extends A2A_INFO_ResponsePacket
{	
	/**
	 * 
	 */
	public function __construct($data)
	{
		parent::__construct(SteamPacket::A2A_INFO_SOURCE_RESPONSE_HEADER, $data);
		
	  $this->networkVersion = $this->contentData->getByte();
    $this->serverName = $this->contentData->getString();
    $this->mapName = $this->contentData->getString();
    $this->gameDir = $this->contentData->getString();
    $this->gameDesc = $this->contentData->getString();
    $this->appId = $this->contentData->getShort();
    $this->playerNumber = $this->contentData->getByte();
    $this->maxPlayers = $this->contentData->getByte();
    $this->botNumber = $this->contentData->getByte();
    $this->dedicated = chr($this->contentData->getByte());
    $this->operatingSystem = chr($this->contentData->getByte());
    $this->passwordProtected = $this->contentData->getByte() == 1;
    $this->secureServer = $this->contentData->getByte() == 1;
    $this->gameVersion = $this->contentData->getString();
    
    if($this->contentData->remaining() > 0)
    {
    	$extraDataFlag = $this->contentData->getByte();
    	
	    if($extraDataFlag & 0x80)
	    {
	      $this->serverPort = $this->contentData->getShort();
	    }
	    
	    if($extraDataFlag & 0x40)
	    {
	      $this->tvPort = $this->contentData->getShort();
	      $this->tvName = $this->contentData->getString();
	    }
	    
	    if($extraDataFlag & 0x20)
	    {
	      $this->serverTags = $this->contentData->getString();
	    }
	  }
	}
}
?>
