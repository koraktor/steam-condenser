<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 *
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */

/**
 * The SteamGroup class represents a group in the Steam Community
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class SteamGroup {

    /**
     * @var String
     */
    private $customUrl;

    /**
     * @var int
     */
    private $groupId64;

    /**
     * @var array
     */
    private $members;

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

        if($fetch) {
            $this->fetchMembers();
        }
    }

    /**
     * Parses the data about this groups members
     */
    public function fetchMembers() {
        $this->members = array();
        $page = 1;

        do {
            $page++;
            $url = "{$this->getBaseUrl()}/memberslistxml?p=$page";
            var_dump($url);
            $memberData = new SimpleXMLElement(file_get_contents($url));

            $totalPages = (int) $memberData->totalPages;

            foreach($memberData->members as $member) {
                array_push($this->members, new SteamId($member->steamID64, false));
            }
        } while($page <= $totalPages);
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
        if(empty($this->members) || empty($this->members[0])) {
            $this->fetchMembers();
        }
        return $this->members;
    }
}
?>
