# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2009-2011, Sebastian Staudt

require 'test/unit'

$:.push File.join(File.dirname(__FILE__), '..', 'lib')

require 'core_ext/stringio'

class StringIOTests < Test::Unit::TestCase

  def test_allocate
    buffer = StringIO.alloc 10
    assert_equal("\0" * 10, buffer.string)
  end

  def test_byte
    buffer = StringIO.new('test')
    assert_equal('t'[0], buffer.byte)
    assert_equal(3, buffer.remaining)
  end

  def test_float
    buffer = StringIO.new('test')
    assert_equal('7.71353668494131e+31', buffer.float.to_s)
    assert_equal(0, buffer.remaining)
  end

  def test_long
    buffer = StringIO.new('test')
    assert_equal(1953719668, buffer.long)
    assert_equal(0, buffer.remaining)
  end

  def test_short
    buffer = StringIO.new('test')
    assert_equal(25972, buffer.short)
    assert_equal(2, buffer.remaining)
  end

  def test_signed_long
    buffer = StringIO.new("   \255")
    assert_equal(-1390403552, buffer.signed_long)
    assert_equal(0, buffer.remaining)
  end

  def test_string
    buffer = StringIO.new("test\0test")
    assert_equal('test', buffer.cstring)
    assert_equal(4, buffer.remaining)
  end

  def test_put
    buffer = StringIO.new('te')
    buffer.write('st')
    assert_equal('st', buffer.string)
    buffer = StringIO.alloc 4
    buffer.write('test')
    assert_equal('test', buffer.string)
  end

  def test_rewind
    buffer = StringIO.new('test')
    assert_equal(25972, buffer.short)
    buffer.rewind
    assert_equal(25972, buffer.short)
  end

  def test_wrap
    string = 'test'
    buffer = StringIO.new(string)
    assert_equal(string, buffer.string)
  end

end
