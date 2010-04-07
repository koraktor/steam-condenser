# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'abstract_class'
require 'datagram_channel'
require 'ipaddr'
require 'stringio_additions'
require 'exceptions/timeout_exception'

# Defines common methods for sockets used to connect to game and master
# servers.
class SteamSocket

  include AbstractClass

  def initialize(*args)
    @channel = DatagramChannel.open
    @channel.connect *args
    @channel.configure_blocking false

    @remote_socket = Socket.getaddrinfo args[0].to_s, args[1]
  end

  # Abstract +reply+ method
  def reply
    raise NotImplementedError
  end

  def receive_packet(buffer_length = 0)
    raise TimeoutException if select([@channel.socket], nil, nil, 1).nil?

    if buffer_length == 0
      @buffer.truncate @buffer.size
    else
      @buffer = StringIO.allocate buffer_length
    end

    bytes_read = @channel.read @buffer
    @buffer.rewind

    bytes_read
  end

  def send(data_packet)
    puts "Sending data packet of type \"#{data_packet.class.to_s}\"." if $DEBUG

    @buffer = StringIO.new data_packet.to_s
    @channel.write @buffer
  end

  def finalize
    @channel.close
  end

end
