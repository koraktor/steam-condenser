# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2010, Sebastian Staudt

require 'stringio'

class StringIO

  def self.allocate(size)
    new "\0" * size
  end

  def byte
    read(1)[0]
  end

  def float
    read(4).unpack('e')[0]
  end

  def get
    read remaining
  end

  def long
    read(4).unpack('V')[0]
  end

  def remaining
    size - pos
  end

  def short
    read(2).unpack('v')[0]
  end

  def signed_long
    read(4).unpack('l')[0]
  end

  def cstring
    gets("\0")[0..-2]
  end

end
