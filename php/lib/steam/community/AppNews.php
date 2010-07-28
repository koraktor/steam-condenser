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

require_once STEAM_CONDENSER_PATH . 'steam/community/WebApi.php';

/**
 * The AppNews class is a representation of Steam news and can be used to load
 * current news about specific games
 *
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class AppNews {

    /**
     * @var int
     */
    private $appId;

    /**
     * @var String
     */
    private $author;

    /**
     * @var String
     */
    private $contents;

    /**
     * @var int
     */
    private $date;

    /**
     * @var bool
     */
    private $external;

    /**
     * @var String
     */
    private $feedLabel;

    /**
     * @var String
     */
    private $feedName;

    /**
     * @var String
     */
    private $gid;

    /**
     * @var String
     */
    private $title;

    /**
     * @var String
     */
    private $url;

    /**
     * Loads the news for the given game with the given restrictions
     *
     * @param int appId The unique Steam Application ID of the game (e.g. 440
     *                  for Team Fortress 2). See
     *                  http://developer.valvesoftware.com/wiki/Steam_Application_IDs
     *                  for all application IDs
     * @param int count The maximum number of news to load (default: 5).
     *                  There's no reliable way to load all news. Use really a
     *                  really great number instead
     * @param int maxLength The maximum content length of the news (default:
     *                      null). If a maximum length is defined, the content
     *                      of the news will only be at most maxLength
     *                      characters long plus an ellipsis
     * @return AppNews[] An array of news items for the specified game with the
     *                   given options
     */
    public static function getNewsForApp($appId, $count = 5, $maxLength = null) {
        $params = array('appid' => $appId, 'count' => $count,
                        'maxlength' => $maxLength);
        $data = json_decode(WebApi::getJSON('ISteamNews', 'GetNewsForApp', 1, $params));

        $newsItems = array();
        foreach($data->appnews->newsitems->newsitem as $newsData) {
            $newsItems[] = new AppNews($appId, $newsData);
        }

        return $newsItems;
    }

    /**
     * Creates a new instance of an AppNews news item with the given data
     *
     * @param int appId The unique Steam Application ID of the game (e.g. 440
     *                  for Team Fortress 2). See
     *                  http://developer.valvesoftware.com/wiki/Steam_Application_IDs
     *                  for all application IDs
     * @param stdClass newsData The news data extracted from JSON
     */
    private function __construct($appId, $newsData) {
        $this->appId     = $appId;
        $this->author    = $newsData->author;
        $this->contents  = trim($newsData->contents);
        $this->date      = (int) $newsData->date;
        $this->external  = (bool) $newsData->is_external_url;
        $this->feedLabel = $newsData->feedlabel;
        $this->feedName  = $newsData->feedname;
        $this->gid       = $newsData->gid;
        $this->title     = $newsData->title;
        $this->url       = $newsData->url;
    }

    /**
     * Returns the unique Application ID of the game this news is about
     *
     * @return int The Application ID
     */
    public function getAppId() {
        return $this->appId;
    }

    /**
     * Returns the author of this news item
     *
     * @return String The author of this news
     */
    public function getAuthor() {
        return $this->author;
    }

    /**
     * Returns the content of this news item
     *
     * Might be truncated if maxLength is set when using
     * AppNews.getNewsForApp()
     *
     * @return String The content of this news
     */
    public function getContents() {
        return $this->contents;
    }

    /**
     * Returns the timestamp this news item has been posted
     *
     * @return int The timestamp of this news
     */
    public function getDate() {
        return $this->date;
    }

    /**
     * Returns the label of the category this news has been posted in
     *
     * @return String The title of this news' category
     */
    public function getFeedLabel() {
        return $this->feedLabel;
    }

    /**
     * Returns the name of the category this news has been posted in
     *
     * @return String The String identifier of this news' category
     */
    public function getFeedName() {
        return $this->feedName;
    }

    /**
     * Returns the global identifier of this news
     *
     * @return String The GID of this news
     */
    public function getGid() {
        return $this->gid;
    }

    /**
     * Returns the title of this news item
     *
     * @return String The title of this news
     */
    public function getTitle() {
        return $this->title;
    }

    /**
     * A direct link to the news on the Steam website or a redirecting link to
     * the external post
     *
     * @return String The URL to the original news
     */
    public function getUrl() {
        return $this->url;
    }

    /**
     * Returns whether this news items originates from a source other than Steam
     * itself (e.g. an external blog)
     *
     * @return bool Whether this news has been posted externally
     */
    public function isExternal() {
        return $this->external;
    }

}
?>
