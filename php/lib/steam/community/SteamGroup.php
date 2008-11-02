<?php
/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * @author Sebastian Staudt
 * @license http://www.opensource.org/licenses/bsd-license.php New BSD License
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 * @version $Id$
 */

/**
 * The SteamGroup class represents a group in the Steam Community
 * @package Steam Condenser (PHP)
 * @subpackage Steam Community
 */
class SteamGroup
{
	/**
	 * Creates a SteamGroup object with the given group ID
	 * @param $id
	 */
	public function __construct($id)
	{
		$this->id = $id;		
	}
}
?>
