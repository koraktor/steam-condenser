# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'socket_channel'
require 'exceptions/rcon_ban_exception'
require 'steam/packets/rcon/rcon_packet'
require 'steam/packets/rcon/rcon_packet_factory'
require 'steam/sockets/steam_socket'

class RCONSocket < SteamSocket

  def initialize(ip_address, port_number)
    super ip_address, port_number
    @channel = SocketChannel.open
  end

  def send(data_packet)
    @channel.connect @remote_socket unless @channel.connected?

    @buffer = StringIO.new data_packet.bytes
    @channel.write @buffer
  end

  def reply
    raise RCONBanException if receive_packet(1440) == 0

    packet_data = @buffer.get
    packet_size = @buffer.long + 4
    @buffer.rewind

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
