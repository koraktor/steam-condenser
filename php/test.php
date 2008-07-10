<?php
/**
 * @author Sebastian Staudt
 * @package Steam Interface Package (PHP)
 * @version $Id: test.php 26 2008-03-07 14:50:55Z koraktor $
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

var_dump($server);
?>