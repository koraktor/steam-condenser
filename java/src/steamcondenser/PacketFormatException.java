/** 
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 */

package steamcondenser;

/**
 * @author Sebastian Staudt
 * @version $Id$
 */
public class PacketFormatException extends SteamCondenserException
{
	private static final long serialVersionUID = 7392075651820982928L;
	
	public PacketFormatException(String message)
	{
		super(message);
	}
}
