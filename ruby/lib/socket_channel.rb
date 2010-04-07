# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'ipaddr'
require 'socket'
require 'timeout'

require 'stringio_additions'

class SocketChannel

  attr_reader :socket

  def self.open
    SocketChannel.new
  end

  def close
    @socket.close
  end

  def connect(*args)
    timeout(1) do
      @socket = TCPSocket.new args[0][0][3], args[0][0][1]
      @connected = true
    end

    self
  end

  def initialize
    @connected = false
  end

  def connected?
    @connected
  end

  def read(destination_buffer)
    raise ArgumentError unless destination_buffer.is_a? StringIO

    data = @socket.recv destination_buffer.remaining
    length = destination_buffer.write data
    destination_buffer.truncate length
  end

  def write(source_buffer)
    raise ArgumentError unless source_buffer.is_a? StringIO

    @socket.send(source_buffer.get, 0)
  end

end
