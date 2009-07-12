# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

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
    if !@channel.connected?
      @channel.connect @remote_socket
    end
    
    @buffer = ByteBuffer.wrap data_packet.get_bytes
    @channel.write @buffer
  end
  
  def get_reply
    if self.receive_packet(1440) == 0
      raise RCONBanException
    end
    packet_data = @buffer.array[0..@buffer.limit]
    packet_size = @buffer.get_long + 4
    
    if packet_size > 1440
      remaining_bytes = packet_size - 1440
      begin
        if remaining_bytes < 1440
          self.receive_packet remaining_bytes
        else
          self.receive_packet 1440
        end
        packet_data << @buffer.array[0..@buffer.limit]
        remaining_bytes -= @buffer.limit
      end while remaining_bytes > 0
    end
    
    return RCONPacketFactory.get_packet_from_data(packet_data)
  end
  
end