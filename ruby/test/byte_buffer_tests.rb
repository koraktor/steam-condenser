# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009, Sebastian Staudt

$:.push File.join(File.dirname(__FILE__), '..', 'lib')

require 'test/unit'
require 'byte_buffer'

class ByteBufferTests < Test::Unit::TestCase

  def test_allocate
    buffer = ByteBuffer.allocate(10)
    assert_equal("\0" * 10, buffer.array)
  end

  def test_get_byte
    buffer = ByteBuffer.wrap('test')
    assert_equal('t'[0], buffer.get_byte)
    assert_equal(3, buffer.remaining)
  end

  def test_get_float
    buffer = ByteBuffer.wrap('test')
    assert_equal('7.71353668494131e+31', buffer.get_float.to_s)
    assert_equal(0, buffer.remaining)
  end

  def test_get_long
    buffer = ByteBuffer.wrap('test')
    assert_equal(1953719668, buffer.get_long)
    assert_equal(0, buffer.remaining)
  end

  def test_get_short
    buffer = ByteBuffer.wrap('test')
    assert_equal(25972, buffer.get_short)
    assert_equal(2, buffer.remaining)
  end

  def test_get_signed_long
    buffer = ByteBuffer.wrap("   \255")
    assert_equal(-1390403552, buffer.get_signed_long)
    assert_equal(0, buffer.remaining)
  end

  def test_get_string
    buffer = ByteBuffer.wrap("test\0test")
    assert_equal('test', buffer.get_string)
    assert_equal(4, buffer.remaining)
  end

  def test_put
    buffer = ByteBuffer.wrap('te')
    buffer.put('st')
    assert_equal('st', buffer.array)
    buffer = ByteBuffer.allocate(4)
    buffer.put('test')
    assert_equal('test', buffer.array)
  end

  def test_rewind
    buffer = ByteBuffer.wrap('test')
    assert_equal(25972, buffer.get_short)
    buffer.rewind
    assert_equal(25972, buffer.get_short)
  end

  def test_wrap
    string = 'test'
    buffer = ByteBuffer.wrap(string)
    assert_equal(string, buffer.array)
  end

end
