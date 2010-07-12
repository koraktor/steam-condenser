/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser;

/**
 * @author Sebastian Staudt
 */
public class Helper
{
    public static byte[] byteArrayFromInteger(int integer)
    {
	return new byte[] {
		(byte) (integer >> 24),
		(byte) (integer >> 16),
		(byte) (integer >> 8),
		(byte) integer
	};
    }

    public static int integerFromByteArray(byte[] byteArray)
    {
	return	byteArray[0] << 24 |
	(byteArray[1] & 0xff) << 16 |
	(byteArray[2] & 0xff) << 8 |
	(byteArray[3] & 0xff);
    }
}
