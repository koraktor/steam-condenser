# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

# The ByteBuffer class derived from Java's java.nio.ByteBuffer
class ByteBuffer

  attr_accessor :limit
  attr_reader :position
  protected :initialize
  
  # Creates a new buffer with the given size in bytes
  def self.allocate(length)
    return ByteBuffer.new(0.chr * length)
  end
  
  # Creates a new buffer from an existing String  
  def self.wrap(byte_array)
    return ByteBuffer.new(byte_array.to_s)
  end
  
  # 
  def initialize(byte_array)
    if byte_array.is_a? ByteBuffer
      raise Exception.new
    end
    
    @byte_array = byte_array.to_s
    @capacity = @byte_array.length
    @limit = @capacity
    @position = 0
    @mark = -1
  end
  
  def array
    return @byte_array
  end
  
  def clear
    @limit = @capacity
    @position = 0
    @mark = -1
    
    return self
  end
  
  def flip
    @limit = @position
    @position = 0
    @mark = -1
    
    return self
  end
  
  def get(length = nil)
    if length == nil
      length = @limit - @position
    elsif length > self.remaining
      BufferUnderflowException.new
    end
    
    data = @byte_array[@position, length]
    @position += length
    
    return data
  end
  
  def get_byte
    return self.get(1)[0]
  end
  
  def get_float
    return self.get_long.to_f
  end
  
  def get_long
    return self.get(4).unpack("l")[0]
  end

  def get_short
    return self.get(2).unpack("v")[0]
  end
  
  def get_string
    zero_byte_index = @byte_array.index("\0", @position)
    if zero_byte_index == nil
      return ""
    else
      data_string = self.get(zero_byte_index - @position)
      @position += 1    
      return data_string
    end
  end
  
  def remaining
    return @limit - @position
  end
  
  def rewind
    @position = 0
    @mark = -1
    
    return self
  end
  
  def put(source_byte_array)
    new_position = [source_byte_array.length, self.remaining].min

    @byte_array[@position, new_position] = source_byte_array
    @position = new_position
    
    return self
  end

end
