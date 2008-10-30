<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @version $Id$
 */

ini_set("include_path", ini_get("include_path") . ":./lib");

require_once "InetAddress.php";
require_once "steam/servers/GoldSrcServer.php";
require_once "steam/servers/MasterServer.php";
require_once "steam/servers/SourceServer.php";

require_once "PHPUnit/Framework.php";

$GLOBALS["debug"] = true;

include("includes/functions.php");

class Tests extends PHPUnit_Framework_TestCase
{
  /**
   * @expectedException TimeoutException
   */
  public function testInvalidGoldSrcServer()
  {
    $server = new GoldSrcServer(new InetAddress("1.0.0.0"), 27015);
    $server->getPing();
  }

  /**
   * @expectedException TimeoutException
   */
  public function testInvalidSourceServer()
  {
    $server = new SourceServer(new InetAddress("1.0.0.0"), 27015);
    $server->getPing();
  }

  public function testRandomGoldsrcServer()
  {
    $masterServer = new MasterServer(MasterServer::GOLDSRC_MASTER_SERVER);
    $serverArray = $masterServer->getServers();
    $serverAddress = $serverArray[array_rand($serverArray)];
    $server = new GoldSrcServer(new InetAddress($serverAddress[0]), $serverAddress[1]);
    $server->initialize();
    $server->updatePlayerInfo();
    $server->updateRulesInfo();

    echo $server;
  }

  public function testRandomSourceServer()
  {
    $masterServer = new MasterServer(MasterServer::SOURCE_MASTER_SERVER);
    $serverArray = $masterServer->getServers();
    $serverAddress = $serverArray[array_rand($serverArray)];
    $server = new SourceServer(new InetAddress($serverAddress[0]), $serverAddress[1]);
    $server->initialize();
    $server->updatePlayerInfo();
    $server->updateRulesInfo();

    echo $server;
  }

  public function testRconGoldSrcServer()
  {
    $server = new GoldSrcServer(new InetAddress("192.168.0.2"));
    $server->rconAuth("test");
    $rconReply = $server->rconExec("status");
    echo "$rconReply\n";
  }

  public function testRconSourceServer()
  {
    $server = new SourceServer(new InetAddress("192.168.0.2"));
    $server->rconAuth("test");
    $rconReply = $server->rconExec("status");
    echo "$rconReply\n";
  }
}
?>
