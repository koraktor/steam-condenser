<?php
/**
 * @author Sebastian Staudt
 * @package Steam Interface Package (PHP)
 * @version $Id: test.php 28 2008-07-10 16:21:45Z koraktor $
 */

include("includes/functions.php");

//set_exception_handler("exceptionHandler");
if(extension_loaded("spl"))
{
	spl_autoload_register("autoloadLibrary");
}

$server = new SourceServer(new InetAddress("84.45.77.22"), 27045);
$server->initialize();
$server->getRulesInfo();
$server->getPlayerInfo();

var_dump("server", $server);
?>