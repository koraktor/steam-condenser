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

require_once STEAM_CONDENSER_PATH . 'steam/community/SteamId.php';

/**
 * The SteamGroup class represents a group in the Steam Community
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class SteamGroup {

    /**
     * @var array
     */
    private static $steamGroups = array();

    /**
     * @var String
     */
    private $customUrl;

    /**
     * @var int
     */
    private $fetchTime;

    /**
     * @var int
     */
    private $groupId64;

    /**
     * @var array
     */
    private $members;

    /**
     * Returns whether the requested group is already cached
     * @param String id
     */
    public static function isCached($id) {
        return array_key_exists(strtolower($id), self::$steamGroups);
    }

    /**
     * Clears the group cache
     */
    public static function clearCache() {
        self::$steamGroups = array();
    }

    /**
     * This checks the cache for an existing group. If it exists it is returned.
     * Otherwise a new SteamGroup object is created.
     * @param String $id
     * @param boolean $fetch
     * @param boolean $bypassCache
     * @return SteamGroup
     */
    public static function create($id, $fetch = true, $bypassCache = false) {
        $id = strtolower($id);
        if(self::isCached($id) && !$bypassCache) {
            $group = self::$steamGroups[$id];
            if($fetch && !$group->isFetched()) {
                $group->fetchMembers();
            }
            return $group;
        } else {
            return new SteamGroup($id, $fetch);
        }
    }

    /**
     * Creates a SteamGroup object with the given group ID
     * @param String $id
     * @param boolean $fetch
     */
    public function __construct($id, $fetch = true) {
        if(is_numeric($id)) {
            $this->groupId64 = $id;
        }
        else {
            $this->customUrl = $id;
        }

        $this->fetched = false;

        if($fetch) {
            $this->fetchMembers();
        }

        $this->cache();
    }

    /**
     * Saves this SteamGroup in the cache
     */
    public function cache() {
        if(!array_key_exists($this->groupId64, self::$steamGroups)) {
            self::$steamGroups[$this->groupId64] = $this;
            if(!empty($this->customUrl) &&
               !array_key_exists($this->customUrl, self::$steamGroups)) {
               self::$steamGroups[$this->customUrl] = $this;
            }
        }
    }

    /**
     * Parses the data about this groups members
     */
    public function fetchMembers() {
        $this->members = array();
        $page = 0;

        do {
            $page++;
            $url = "{$this->getBaseUrl()}/memberslistxml?p=$page";
            $memberData = new SimpleXMLElement(file_get_contents($url));

            if($page == 1) {
                $this->groupId64 = (string) $memberData->groupID64;
            }
            $totalPages = (int) $memberData->totalPages;

            foreach($memberData->members->steamID64 as $member) {
                array_push($this->members, SteamId::create($member, false));
            }
        } while($page <= $totalPages);

        $this->fetchTime = time();
    }

    /**
     * Returns the URL to the group's Steam Community page
     * @return String
     */
    public function getBaseUrl() {
        if(empty($this->customUrl)) {
            return "http://steamcommunity.com/gid/{$this->groupId64}";
        } else {
            return "http://steamcommunity.com/groups/{$this->customUrl}";
        }
    }

    /**
     * Returns the custom URL of this group
     * @return String
     */
    public function getCustomUrl() {
        return $this->customUrl;
    }

    /**
     * @return int
     */
    public function getFetchTime() {
        return $this->fetchTime;
    }

    /**
     * Returns the 64bit group ID
     * @return int
     */
    public function getGroupId64() {
        return $this->groupId64;
    }

    /**
     * Returns the number of members this group has.
     * If the members have already been fetched with +fetch_members+ the size of
     * the member array is returned. Otherwise the group size is separately
     * fetched.
     * @return int
     */
    public function getMemberCount() {
        if(empty($this->members)) {
            $url = $this->getBaseUrl() . '/memberslistxml';
            $memberData = new SimpleXMLElement(file_get_contents($url));
            return (int) $memberData->memberCount;
        } else {
            return sizeof($this->members);
        }
    }

    /**
     * Returns the members of this group
     * Calls fetchMembers() if the members haven't been fetched already.
     * @return Array
     */
    public function getMembers() {
        if(empty($this->members) || empty($this->members[0])) {
            $this->fetchMembers();
        }
        return $this->members;
    }

    /**
     * Returns whether the data for this group has already been fetched
     * @return boolean
     */
    public function isFetched() {
        return !empty($this->fetchTime);
    }
}
?>
