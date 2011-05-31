# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'stringio_additions'
require 'exceptions/packet_format_exception'
require 'steam/packets/steam_packet_factory'
require 'steam/packets/rcon/rcon_auth_response'
require 'steam/packets/rcon/rcon_exec_response'

# This module provides functionality to handle raw packet data for Source RCON
#
# It's is used to transform data bytes into packet objects for RCON
# communication with Source servers.
#
# @author Sebastian Staudt
# @see RCONPacket
module RCONPacketFactory

  # Creates a new packet object based on the header byte of the given raw data
  #
  # @param [String] raw_data The raw data of the packet
  # @raise [PacketFormatException] if the packet header is not recognized
  # @return [RCONPacket] The packet object generated from the packet data
  def self.packet_from_data(raw_data)
    byte_buffer = StringIO.new raw_data

    request_id = byte_buffer.long
    header = byte_buffer.long
    data = byte_buffer.cstring

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
