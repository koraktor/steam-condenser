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
 * @subpackage Steam Community
 */

require_once "steam/community/GameAchievement.php";

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
            case 'cs:s':
                require_once 'steam/community/css/CSSStats.php';
                return new CSSStats($steamId);
            case 'defensegrid:awakening':
                require_once 'steam/community/defense_grid/DefenseGridStats.php';
                return new DefenseGridStats($steamId);
            case 'dod:s':
                require_once 'steam/community/dods/DoDSStats.php';
                return new DoDSStats($steamId);
            case 'l4d':
                require_once 'steam/community/l4d/L4DStats.php';
                return new L4DStats($steamId);
            case 'l4d2':
                require_once 'steam/community/l4d/L4D2Stats.php';
                return new L4D2Stats($steamId);
            case 'tf2':
                require_once 'steam/community/tf2/TF2Stats.php';
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
        $this->xmlData = new SimpleXMLElement(file_get_contents("http://www.steamcommunity.com/id/$steamId/stats/$gameName?xml=1"));

        $this->privacyState = (string) $this->xmlData->privacyState;
        if($this->isPublic()) {
            preg_match('#http://store.steampowered.com/app/([1-9][0-9]*)#', (string) $this->xmlData->game->gameLink, $appId);
            $this->appId = (int) $appId[1];
            $this->customUrl = (string) $this->xmlData->player->customURL;
            $this->gameFriendlyName = (string) $this->xmlData->game->gameFriendlyName;
            $this->gameName = (string) $this->xmlData->game->gameName;
            $this->hoursPlayed = (string) $this->xmlData->stats->hoursPlayed;
            $this->steamId64 = (string) $this->xmlData->player->steamID64;
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
