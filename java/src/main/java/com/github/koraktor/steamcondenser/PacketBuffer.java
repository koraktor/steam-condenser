/**
 * This code is free software; you can redistribute it and/or modify it under
 * the terms of the new BSD License.
 *
 * Copyright (c) 2008-2009, Sebastian Staudt
 */

package com.github.koraktor.steamcondenser;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * A convenience class wrapping around ByteBuffer, for easily retrieving
 * String values
 * @author Sebastian Staudt
 */
public class PacketBuffer
{
    private ByteBuffer byteBuffer;

    /**
     * Creates a new PacketBuffer from the given byte[]
     * @param data The data do build a PackteBuffer on
     */
    public PacketBuffer(byte[] data)
    {
	this.byteBuffer = ByteBuffer.wrap(data);
    }

    /**
     * @return The backing byte[] of this PacketBuffer
     */
    public byte[] array()
    {
	return this.byteBuffer.array();
    }

    /**
     * @return A byte at the buffer's current position
     */
    public byte getByte()
    {
	return this.byteBuffer.get();
    }

    /**
     * @return A floating-point value from the buffer's current position
     */
    public float getFloat()
    {
	return this.byteBuffer.getFloat();
    }

    /**
     * @return An integer from the buffer's current position
     */
    public int getInt()
    {
	return this.byteBuffer.getInt();
    }

    /**
     * @return The length of the buffer
     */
    public int getLength()
    {
	return this.byteBuffer.capacity();
    }

    /**
     * @return A short integer from the buffer's current position
     */
    public short getShort()
    {
	return this.byteBuffer.getShort();
    }

    /**
     * @return A string from the buffer's current position
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
            // Setting new position by byte length of the string for compatibility with multi-byte characters
            this.byteBuffer.position(this.byteBuffer.position() + dataString.getBytes().length + 1);

            return dataString;
        }
    }

    public PacketBuffer order(ByteOrder byteOrder)
    {
	this.byteBuffer.order(byteOrder);
	return this;
    }

    /**
     * @return The number of bytes remaining in this buffer
     */
    public int remaining()
    {
	return this.byteBuffer.remaining();
    }

    /**
     * @return true, if there's at least one byte left remaining in this buffer
     */
    public boolean hasRemaining()
    {
	return this.byteBuffer.hasRemaining();
    }
}
