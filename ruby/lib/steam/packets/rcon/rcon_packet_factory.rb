# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt

require "abstract_class"
require "byte_buffer"
require "exceptions/packet_format_exception"
require "steam/packets/steam_packet_factory"
require "steam/packets/rcon/rcon_auth_response"
require "steam/packets/rcon/rcon_exec_response"

class RCONPacketFactory < SteamPacketFactory
  
  include AbstractClass
  
  def self.get_packet_from_data(raw_data)
    byte_buffer = ByteBuffer.new raw_data
    
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
  
end