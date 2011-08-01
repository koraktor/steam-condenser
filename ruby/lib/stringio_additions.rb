# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2010-2011, Sebastian Staudt

require 'stringio'

# This extends the `StringIO` class from Ruby's standard library. It adds some
# methods to handle byte-wise input from a `StringIO` object.
#
# @author Sebastian Staudt
class StringIO

  # Creates a new instance of `StringIO` with the given size and fills it with
  # zero-bytes.
  #
  # @param [Fixnum] size The size the new instance should have
  # @return [StringIO] A new `StringIO` instance with the given size, filled
  #         with zero-bytes
  def self.alloc(size)
    new "\0" * size
  end

  # Reads a single byte from the current position of the byte stream
  #
  # @return [Fixnum] The numeric value of the byte at the current position
  def byte
    read(1)[0].ord
  end

  # Reads a floating-point integer (32 bit) from the current position of the
  # byte stream
  #
  # @return [Float] The floating-point integer read from the byte stream
  def float
    read(4).unpack('e')[0]
  end

  # Reads the whole remaining content of the byte stream from the current
  # position to the end
  #
  # @return [String] The remainder of the byte stream starting from the current
  #         position of the byte stream
  def get
    read remaining
  end

  # Reads an unsigned long integer (32 bit) from the current position of the
  # byte stream
  #
  # @return [Fixnum] The unsigned long integer read from the byte stream
  def long
    read(4).unpack('V')[0]
  end

  # Returns the remaining number of bytes from the current position to the end
  # of the byte stream
  #
  # @return [Fixnum] The number of bytes until the end of the stream
  def remaining
    size - pos
  end

  # Reads an unsigned short integer (16 bit) from the current position of the
  # byte stream
  #
  # @return [Fixnum] The unsigned short integer read from the byte stream
  def short
    read(2).unpack('v')[0]
  end

  # Reads a signed long integer (32 bit) from the current position of the byte
  # stream
  #
  # @return [Fixnum] The signed long integer read from the byte stream
  def signed_long
    read(4).unpack('l')[0]
  end

  # Reads a zero-byte terminated string from the current position of the byte
  # stream
  #
  # This reads the stream up until the first occurance of a zero-byte or the
  # end of the stream. The zero-byte is not included in the returned string.
  #
  # @return [String] The zero-byte terminated string read from the byte stream
  def cstring
    gets("\0")[0..-2]
  end

end
