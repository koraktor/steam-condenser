# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'ipaddr'
require 'socket'

require 'stringio_additions'

class DatagramChannel

  attr_reader :socket

  def self.open
    DatagramChannel.new
  end

  def close
    @socket.close
  end

  def connect(*args)
    if args[0].is_a? IPAddr
      ip_address = args[0].to_s
    elsif args[0].is_a? String
      ip_address = IPSocket.getaddress args[0]
    else
      raise ArgumentError
    end

    if args[1].is_a? Numeric
      port_number = args[1]
    else
      port_number = args[1].to_i
    end

    @socket.connect ip_address, port_number

    self
  end

  def read(destination_io)
    raise ArgumentError unless destination_io.is_a? StringIO

    data = @socket.recv destination_io.remaining
    length = destination_io.write data
    destination_io.truncate length
  end

  def write(source_io)
    raise ArgumentError unless source_io.is_a? StringIO

    @socket.send(source_io.get, 0)
  end

  protected

  def initialize
    @socket = UDPSocket.new
  end

end
