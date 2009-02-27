<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 *
 * @author  Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @version $Id$
 */

ini_set("include_path", ini_get("include_path") . PATH_SEPARATOR . dirname(__FILE__) . "/../lib");
error_reporting(E_ALL & ~E_USER_NOTICE);

require_once "InetAddress.php";
require_once "steam/servers/GoldSrcServer.php";
require_once "steam/servers/SourceServer.php";

require_once "PHPUnit/Framework.php";

/**
 * @package    Steam Condenser (PHP)
 * @subpackage Tests
 */
class RCONTests extends PHPUnit_Framework_TestCase
{
    public function testRconLongGoldSrcServer()
    {
        $server = new GoldSrcServer(new InetAddress("127.0.0.1"));
        $server->rconAuth("test");
        $rconReply = $server->rconExec("cvarlist");
        echo "$rconReply\n";
        $this->assertTrue(
            strpos($rconReply, "CvarList ? for syntax") !== false,
            "Did not receive complete cvarlist.");
    }

    public function testRconLongSourceServer()
    {
        $server = new SourceServer(new InetAddress("127.0.0.1"));
        $server->rconAuth("test");
        $rconReply = $server->rconExec("cvarlist");
        echo "$rconReply\n";
        $this->assertTrue(
            strpos($rconReply, "total convars/concommands"),
            "Did not receive complete cvarlist.");
    }

    public function testRconShortGoldSrcServer()
    {
        $server = new GoldSrcServer(new InetAddress("127.0.0.1"));
        $server->rconAuth("test");
        $rconReply = $server->rconExec("version");
        echo "$rconReply\n";
        $this->assertTrue(
            strpos($rconReply, "Protocol version") !== false &&
            strpos($rconReply, "Exe version") !== false &&
            strpos($rconReply, "Exe build") !== false,
            "Did not receive correct version response.");
    }

    public function testRconShortSourceServer()
    {
        $server = new SourceServer(new InetAddress("127.0.0.1"));
        $server->rconAuth("test");
        $rconReply = $server->rconExec("version");
        echo "$rconReply\n";
        $this->assertTrue(
            strpos($rconReply, "Protocol version") !== false &&
            strpos($rconReply, "Exe version") !== false &&
            strpos($rconReply, "Exe build") !== false,
            "Did not receive correct version response.");
    }
}
?>
