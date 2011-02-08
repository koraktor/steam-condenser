# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'ipaddr'
require 'socket'
require 'timeout'

require 'exceptions/rcon_ban_exception'
require 'steam/packets/rcon/rcon_packet'
require 'steam/packets/rcon/rcon_packet_factory'
require 'steam/sockets/steam_socket'

class RCONSocket

  include SteamSocket

  def initialize(ip, port)
    ip = IPSocket.getaddress(ip) unless ip.is_a? IPAddr

    @ip     = ip
    @port   = port
    @socket = nil
  end

  def connect
    begin
      timeout(@@timeout / 1000.0) { @socket = TCPSocket.new @ip, @port }
    rescue Timeout::Error
      raise TimeoutException
    end
  end

  def send(data_packet)
    connect if @socket.nil? || @socket.closed?

    super
  end

  def reply
    raise RCONBanException if receive_packet(1440) == 0

    packet_data = @buffer.get
    @buffer.rewind
    packet_size = @buffer.long + 4

    if packet_size > 1440
      remaining_bytes = packet_size - @buffer.size
      begin
        if remaining_bytes < 1440
          receive_packet remaining_bytes
        else
          receive_packet 1440
        end
        packet_data << @buffer.get
        remaining_bytes -= @buffer.size
      end while remaining_bytes > 0
    end

    RCONPacketFactory.packet_from_data(packet_data)
  end

end
