/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser;

/**
 * A helper class used to convert byte arrays into integers and vice-versa
 *
 * @author Sebastian Staudt
 */
public abstract class Helper {

    /**
     * Convert an integer value into the corresponding byte array
     *
     * @param integer The integer to convert
     * @return The byte array representing the given integer
     */
    public static byte[] byteArrayFromInteger(int integer) {
        return new byte[] {
            (byte) (integer >> 24),
            (byte) (integer >> 16),
            (byte) (integer >> 8),
            (byte) integer
        };
    }

    /**
     * Convert a byte array into the corresponding integer value of its bytes
     *
     * @param byteArray The byte array to convert
     * @return The integer represented by the byte array
     */
    public static int integerFromByteArray(byte[] byteArray) {
        return byteArray[0] << 24 |
               (byteArray[1] & 0xff) << 16 |
               (byteArray[2] & 0xff) << 8 |
               (byteArray[3] & 0xff);
    }
}
