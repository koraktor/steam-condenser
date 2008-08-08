#!/usr/bin/env php
<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @version $Id$
 */

$GLOBALS["debug"] = true; 

include("includes/functions.php");

//set_exception_handler("exceptionHandler");
if(extension_loaded("spl"))
{
	spl_autoload_register("autoloadLibrary");
}

$server = new SourceServer(new InetAddress("84.45.77.22"), 27045);
$server->initialize();
$server->updateRulesInfo();
$server->updatePlayerInfo();

var_dump($server)
?>