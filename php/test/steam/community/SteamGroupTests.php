<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009, Sebastian Staudt
 *
 * @author  Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 */

ini_set("include_path", ini_get("include_path") . PATH_SEPARATOR . dirname(__FILE__) . "/../../../lib");
error_reporting(E_ALL & ~E_USER_NOTICE);

require_once "steam/community/SteamGroup.php";

require_once "PHPUnit/Framework.php";

/**
 * @package    Steam Condenser (PHP)
 * @subpackage Tests
 */
class SteamGroupTests extends PHPUnit_Framework_TestCase {

    public function testCache() {
        SteamGroup::clearCache();
        $group = SteamGroup::create('valve');
        $fetchTime = $group->getFetchTime();
        $this->assertTrue(SteamGroup::isCached('103582791429521412'));
        $this->assertTrue(SteamGroup::isCached('valve'));
        $group = SteamGroup::create('valve');
        $this->assertEquals($fetchTime, $group->getFetchTime());
    }

    public function testCaseInsensitivity() {
        SteamGroup::clearCache();
        $group = SteamGroup::create('valve', false);
        $group2 = SteamGroup::create('Valve', false);
        $group3 = SteamGroup::create('VALVE', false, true);
        $this->assertTrue(SteamGroup::isCached('VALVE'));
        $this->assertEquals($group, $group2);
        $this->assertEquals($group, $group3);
    }

    public function testBypassCache() {
        SteamGroup::clearCache();
        $group = SteamGroup::create('valve');
        $fetchTime = $group->getFetchTime();
        sleep(1);
        $group = SteamGroup::create('valve', true, true);
        $this->assertGreaterThan($fetchTime, $group->getFetchTime());
    }
}
?>