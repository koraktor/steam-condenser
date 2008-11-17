<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 * 
 * @author  Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @version $Id$
 */

ini_set("include_path", ini_get("include_path") . ":" . dirname(__FILE__) . "/../lib");
error_reporting(E_ALL & ~E_USER_NOTICE);

require_once "InetAddress.php";
require_once "steam/servers/GoldSrcServer.php";
require_once "steam/servers/SourceServer.php";

require_once "PHPUnit/Framework.php";

/**
 * @package    Steam Condenser (PHP)
 * @subpackage Tests 
 */
class QueryTests extends PHPUnit_Framework_TestCase
{
  /*public function testRconGoldSrcServer()
  {
    $server = new GoldSrcServer(new InetAddress("192.168.0.166"));
    $server->rconAuth("test");
    $rconReply = $server->rconExec("status");
    echo "$rconReply\n";
  }*/

  public function testRconSourceServer()
  {
    $server = new SourceServer(new InetAddress("192.168.0.166"));
    $server->rconAuth("test");
    $rconReply = $server->rconExec("version");
    echo "$rconReply\n";
  }
}
?>