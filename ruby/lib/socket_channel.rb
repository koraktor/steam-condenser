# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "byte_buffer"

class SocketChannel
  
  attr_reader :socket
  
  def self.open
    return SocketChannel.new
  end
  
  def connect(*args)
    @socket = TCPSocket.new args[0][0][3], args[0][0][1]
    @connected = true
    
    return self
  end
  
  def initialize
    @connected = false
  end
  
  def connected?
    return @connected
  end
  
  def read(destination_buffer)
    if !destination_buffer.is_a? ByteBuffer
      raise ArgumentError
    end
    
    length = destination_buffer.remaining
    data = @socket.recv length
    destination_buffer.put data
    
    return data.length
  end
  
  def write(source_buffer)
    if !source_buffer.is_a? ByteBuffer
      raise ArgumentError
    end
    
    return @socket.send(source_buffer.get, 0)
  end
  
end