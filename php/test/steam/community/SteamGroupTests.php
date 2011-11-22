<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2009-2011, Sebastian Staudt
 *
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 */

error_reporting(E_ALL & ~E_USER_NOTICE);

require_once dirname(__FILE__) . '/../../../lib/steam-condenser.php';
require_once STEAM_CONDENSER_PATH . 'steam/community/SteamGroup.php';

require_once 'PHPUnit/Framework.php';

/**
 * @author     Sebastian Staudt
 * @package    steam-condenser
 * @subpackage tests
 */
class SteamGroupTests extends PHPUnit_Framework_TestCase {

    public function testBypassCache() {
        SteamGroup::clearCache();
        $group = SteamGroup::create('valve');
        $fetchTime = $group->getFetchTime();
        sleep(1);
        $group = SteamGroup::create('valve', true, true);
        $this->assertGreaterThan($fetchTime, $group->getFetchTime());
    }

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
        $this->assertTrue(SteamGroup::isCached('valve'));
        $this->assertEquals($group, $group2);
        $this->assertEquals($group, $group3);
    }
}
