# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require 'ipaddr'
require 'socket'
require 'timeout'

require 'byte_buffer'

class SocketChannel
  
  attr_reader :socket
  
  def self.open
    return SocketChannel.new
  end
  
  def connect(*args)
    timeout(1) do
      @socket = TCPSocket.new args[0][0][3], args[0][0][1]
      @connected = true
    end
    
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