# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "socket_channel"
require "steam/packets/rcon/rcon_packet"
require "steam/packets/rcon/rcon_packet_factory"
require "steam/sockets/steam_socket"

class RCONSocket < SteamSocket
  
  def initialize(ip_address, port_number)
    super ip_address, port_number
    
    @buffer = ByteBuffer.allocate 1400
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
    @buffer = ByteBuffer.allocate 1400
    @channel.read @buffer
    
    return RCONPacketFactory.get_packet_from_data @buffer.array
  end
  
end