# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'ipaddr'
require 'socket'

require 'core_ext/stringio'
require 'exceptions/timeout_exception'

# This module implements common functionality for sockets used to connect to
# game and master servers
#
# @author Sebastian Staudt
module SteamSocket

  # The default socket timeout
  @@timeout = 1000

  # Sets the timeout for socket operations
  #
  # Any request that takes longer than this time will cause a
  # {TimeoutException}.
  #
  # @param [Fixnum] timeout The amount of milliseconds before a request times
  #        out
  def self.timeout=(timeout)
    @@timeout = timeout
  end

  # Creates a new UDP socket to communicate with the server on the given IP
  # address and port
  #
  # @param [String] ip_address Either the IP address or the DNS name of the
  #        server
  # @param [Fixnum] port The port the server is listening on
  def initialize(ip_address, port = 27015)
    @socket = UDPSocket.new
    @socket.connect ip_address, port
  end

  # Closes the underlying socket
  def close
    @socket.close
  end

  # Reads the given amount of data from the socket and wraps it into the buffer
  #
  # @param [Fixnum] buffer_length The data length to read from the socket
  # @raise [TimeoutException] if no packet is received on time
  # @return [Fixnum] The number of bytes that have been read from the socket
  # @see StringIO
  def receive_packet(buffer_length = 0)
    if select([@socket], nil, nil, @@timeout / 1000.0).nil?
      raise TimeoutException
    end

    if buffer_length == 0
      @buffer.rewind
    else
      @buffer = StringIO.alloc buffer_length
    end

    data = @socket.recv @buffer.remaining
    bytes_read = @buffer.write data
    @buffer.truncate bytes_read
    @buffer.rewind

    bytes_read
  end

  # Sends the given packet to the server
  #
  # This converts the packet into a byte stream first before writing it to the
  # socket.
  #
  # @param [SteamPacket] data_packet The packet to send to the server
  # @see SteamPacket#to_s
  def send(data_packet)
    puts "Sending data packet of type \"#{data_packet.class.to_s}\"." if $DEBUG

    @socket.send data_packet.to_s, 0
  end

end
