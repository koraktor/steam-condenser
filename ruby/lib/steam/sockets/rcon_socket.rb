# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "byte_buffer"
require "socket_channel"
require "exceptions/packet_format_exception"
require "steam/packets/rcon/rcon_auth_response"
require "steam/packets/rcon/rcon_exec_response"
require "steam/packets/rcon/rcon_packet"
require "steam/sockets/steam_socket"

class RCONSocket < SteamSocket
  
  def initialize(ip_address, port_number)
    super ip_address, port_number
    
    @buffer = ByteBuffer.allocate 1400
    @channel = SocketChannel.open
  end
  
  def create_packet
    byte_buffer = ByteBuffer.new @buffer.array
    
    packet_size = byte_buffer.get_long
    request_id = byte_buffer.get_long
    header = byte_buffer.get_long
    data = byte_buffer.get_string
    
    case header
      when RCONPacket::SERVERDATA_AUTH_RESPONSE then
        return RCONAuthResponse.new(request_id)
      when RCONPacket::SERVERDATA_RESPONSE_VALUE then
        return RCONExecResponse.new(request_id, data)
      else
        raise PacketFormatException.new("Unknown packet with header #{header.to_s(16)} received.")
    end
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
    
    return self.create_packet
  end
  
end