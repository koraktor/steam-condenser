# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2009-2011, Sebastian Staudt

require 'helper'
require 'core_ext/stringio'

class TestStringIO < Test::Unit::TestCase

  context 'The StringIO extensions' do

    setup do
      @io = StringIO.new 'test'
    end

    should 'provide `#alloc`' do
      @io = StringIO.alloc 10

      assert_equal "\0" * 10, @io.string
    end

    should 'provide `#byte`' do
      assert_equal 't'.bytes.first, @io.byte
      assert_equal 3, @io.remaining
    end

    should 'provide `#float`' do
      assert_in_delta 7.713536684941307 * 10**31, @io.float, 0.0000000001
      assert_equal 0, @io.remaining
    end

    should 'provide `#long`' do
      assert_equal 1953719668, @io.long
      assert_equal 0, @io.remaining
    end

    should 'provide `#short`' do
      assert_equal 25972, @io.short
      assert_equal 2, @io.remaining
    end

    should 'provide `#signed_long`' do
      @io = StringIO.new "   \255"

      assert_equal -1390403552, @io.signed_long
      assert_equal 0, @io.remaining
    end

    should 'provide `#cstring`' do
      @io = StringIO.new "test\0test"

      assert_equal 'test', @io.cstring
      assert_equal 4, @io.remaining
    end

  end

end
