/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2011, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A convenience class wrapping around {@link ByteBuffer} used for easy
 * retrieval of string values
 *
 * @author Sebastian Staudt
 */
public class PacketBuffer
{
    private ByteBuffer byteBuffer;

    /**
     * Creates a new packet buffer from the given byte array
     *
     * @param data The data wrap into the underlying byte buffer
     */
    public PacketBuffer(byte[] data)
    {
        this.byteBuffer = ByteBuffer.wrap(data);
    }

    /**
     * Returns the backing byte array of the underlying byte buffer
     *
     * @return The backing byte array
     */
    public byte[] array()
    {
        return this.byteBuffer.array();
    }

    /**
     * Returns the next byte at the buffer's current position
     *
     * @return A byte
     */
    public byte getByte()
    {
        return this.byteBuffer.get();
    }

    /**
     * Returns a floating-point value from the buffer's current position
     *
     * @return A floating-point value
     */
    public float getFloat()
    {
        return this.byteBuffer.getFloat();
    }

    /**
     * Returns an integer value from the buffer's current position
     *
     * @return An integer value
     */
    public int getInt()
    {
        return this.byteBuffer.getInt();
    }

    /**
     * Returns the length of this buffer
     *
     * @return The length of this buffer
     */
    public int getLength()
    {
        return this.byteBuffer.capacity();
    }

    /**
     * Returns a short integer value from the buffer's current position
     *
     * @return A short integer value
     */
    public short getShort()
    {
        return this.byteBuffer.getShort();
    }

    /**
     * Returns a string value from the buffer's current position
     * <p>
     * This reads the bytes up to the first zero-byte of the underlying byte
     * buffer into a String
     *
     * @return A string value
     */
    public String getString()
    {
        byte[] remainingBytes = new byte[this.byteBuffer.remaining()];
        this.byteBuffer.slice().get(remainingBytes);
        String dataString = new String(remainingBytes);
        int stringEnd = dataString.indexOf(0);

        if(stringEnd == -1) {
            return null;
        } else {
            dataString = dataString.substring(0, stringEnd);
            this.byteBuffer.position(this.byteBuffer.position() + dataString.getBytes().length + 1);

            return dataString;
        }
    }

    /**
     * Changes the byte-order of the underlying byte buffer
     *
     * @param byteOrder The byte-order to use in the underlying byte buffer
     */
    public PacketBuffer order(ByteOrder byteOrder)
    {
        this.byteBuffer.order(byteOrder);
        return this;
    }

    /**
     * Returns the number of bytes remaining in the underlying byte buffer from
     * the current position up to the end
     *
     * @return The number of bytes remaining in this buffer
     */
    public int remaining()
    {
        return this.byteBuffer.remaining();
    }

    /**
     * Returns whether there is more data available in this buffer after the
     * current position
     *
     * @return <code>true</code> if there's at least one byte left remaining
     */
    public boolean hasRemaining()
    {
        return this.byteBuffer.hasRemaining();
    }
}
