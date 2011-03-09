# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'stringio_additions'
require 'exceptions/packet_format_exception'
require 'steam/packets/steam_packet_factory'
require 'steam/packets/rcon/rcon_auth_response'
require 'steam/packets/rcon/rcon_exec_response'

module RCONPacketFactory

  include SteamPacketFactory

  def self.packet_from_data(raw_data)
    byte_buffer = StringIO.new raw_data

    size = byte_buffer.long
    request_id = byte_buffer.long
    header = byte_buffer.long
    data = byte_buffer.cstring

    return nil if size - 10 != data.size

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
