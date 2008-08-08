<?php
/**
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage functions
 * @version $Id$
 */

/**
 * @param String $className
 */
function __autoload($className)
{
	autoloadLibrary($className);
}

/**
 * @param String $className
 * @param String $baseDirectory
 * @return String
 */
function autoloadLibrary($className, $baseDirectory = null)
{
	if($baseDirectory == null)
	{
		$baseDirectory = dirname($_SERVER["SCRIPT_NAME"]) . "/lib";
	}
	
	$libraryPath = "$baseDirectory/$className.php";
	
	if(file_exists($libraryPath))
	{
		require_once($libraryPath);
		return $libraryPath;
	}
	
	$subDirs = glob("$baseDirectory/*", GLOB_ONLYDIR);
	
	foreach($subDirs as $subDir)
	{
		$libraryPath = autoloadLibrary($className, $subDir);
		
		if($libraryPath != false)
		{
			require_once($libraryPath);
			return $libraryPath;
		}
	}
	
	return false;
}

/**
 * @param Exception $exception
 */
function exceptionHandler(Exception $exception)
{
	echo "Uncaught exception (" . get_class($exception) . ") occured in " . $exception->getFile() . ":" . $exception->getLine() . "\n";
	echo "Stack trace:\n";
	$stackTrace = $exception->getTrace();
	//var_dump($stackTrace);

	foreach($stackTrace as $traceNumber => $tracePoint)
	{
		echo "#$traceNumber: {$tracePoint["file"]}({$tracePoint["line"]}): {$tracePoint["class"]}{$tracePoint["type"]}{$tracePoint["function"]}()\n";
	}
}

/**
 *
 */
function debug($debugMessage)
{
	if($GLOBALS["debug"])
	{
		echo "DEBUG: $debugMessage\n";
	}
}
?>