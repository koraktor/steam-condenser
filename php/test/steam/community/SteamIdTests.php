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

require_once 'PHPUnit/Framework.php';

/**
 * @author     Sebastian Staudt
 * @package    steam-condenser
 * @subpackage tests
 */
class SteamIdTests extends PHPUnit_Framework_TestCase {

    public function testBypassCache() {
        SteamId::clearCache();
        $steamId = SteamId::create('koraktor');
        $fetchTime = $steamId->getFetchTime();
        sleep(1);
        $steamId = SteamId::create('koraktor', true, true);
        $this->assertGreaterThan($fetchTime, $steamId->getFetchTime());
    }

    public function testCache() {
        SteamId::clearCache();
        $steamId = SteamId::create('koraktor');
        $fetchTime = $steamId->getFetchTime();
        $this->assertTrue(SteamId::isCached('76561197961384956'));
        $this->assertTrue(SteamId::isCached('koraktor'));
        sleep(1);
        $steamId = SteamId::create('koraktor');
        $this->assertEquals($fetchTime, $steamId->getFetchTime());
    }

    public function testCaseInsensitivity() {
        SteamId::clearCache();
        $steamId = SteamId::create('koraktor', false);
        $steamId2 = SteamId::create('Koraktor', false);
        $steamId3 = SteamId::create('KORAKTOR', false, true);
        $this->assertTrue(SteamId::isCached('koraktor'));
        $this->assertEquals($steamId, $steamId2);
        $this->assertEquals($steamId, $steamId3);
    }

    public function testConvertSteamIdToCommunityId() {
        $steamId64 = SteamId::convertSteamIdToCommunityId('STEAM_0:0:12345');
        $this->assertType(PHPUnit_Framework_Constraint_IsType::TYPE_STRING,
            $steamId64);
        $this->assertEquals('76561197960290418', $steamId64);
    }
}
