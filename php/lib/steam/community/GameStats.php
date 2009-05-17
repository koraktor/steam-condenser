<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 * @version $Id$
 */

require_once "steam/community/GameAchievement.php";
require_once "steam/community/dods/DoDSStats.php";
require_once "steam/community/l4d/L4DStats.php";
require_once "steam/community/tf2/TF2Stats.php";

/**
 * The GameStats class represents the game statistics for a single user and a
 * specific game
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class GameStats {

    protected $achievements;

    /**
     * Used to cache the XML data of the statistics for this game and this
     * user
     * @var SimpleXML
     */
    protected $xmlData;

    /**
     * Creates a GameStats (or one of its subclasses) object for the given user
     * depending on the game selected
     */
    public static function createGameStats($steamId, $gameName) {
        switch($gameName) {
            case 'dod:s':
                return new DoDSStats($steamId);
            case 'l4d':
                return new L4DStats($steamId);
            case 'tf2':
                return new TF2Stats($steamId);
            default:
                return new GameStats($steamId, $gameName);
        }
    }

    /**
     * Creates a GameStats object and fetchs data from Steam Community for
     * the given user and game
     * @param $steamId
     * @param $gameName
     */
    protected function __construct($steamId, $gameName) {
        $this->steamId = $steamId;

        $this->xmlData = new SimpleXMLElement(file_get_contents("http://www.steamcommunity.com/id/{$this->steamId}/stats/{$gameName}?xml=1"));

        $this->privacyState = (string) $this->xmlData->privacyState;
        if($this->isPublic()) {
            preg_match('#http://store.steampowered.com/app/([1-9][0-9]*)#', (string) $this->xmlData->game->gameLink, $appId);
            $this->appId = (int) $appId[1];
            $this->gameFriendlyName = (string) $this->xmlData->game->gameFriendlyName;
            $this->gameName = (string) $this->xmlData->game->gameName;
            $this->hoursPlayed = (string) $this->xmlData->stats->hoursPlayed;
        }
    }

    /**
     * Returns the achievements for this stats' user and game. If the achievements
     * haven't been parsed already, parsing is done now.
     * @return GameAchievements[]
     */
    public function getAchievements() {
        if(!$this->isPublic()) {
            return;
        }

        if(empty($this->achievements)) {
            $this->achievementsDone = 0;
            foreach($this->xmlData->achievements->children() as $achievement) {
                $this->achievements[] = new GameAchievement($this->steamId, $this->appId, (string) $achievement->name, (bool) $achievement->closed);
                if((bool) $achievement->closed) {
                    $this->achievementsDone += 1;
                }
            }
        }

        return $this->achievements;
    }

    public function isPublic() {
        return $this->privacyState == 'public';
    }
}
?>
