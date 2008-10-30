# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

class SteamPacket
end

require "steam/packets/s2a_info_detailed_packet"
require "steam/packets/a2s_info_packet"
require "steam/packets/s2a_info2_packet"
require "steam/packets/a2a_ping_packet"
require "steam/packets/a2a_ack_packet"
require "steam/packets/a2s_player_packet"
require "steam/packets/s2a_player_packet"
require "steam/packets/a2s_rules_packet"
require "steam/packets/s2a_rules_packet"
require "steam/packets/a2s_serverquery_getchallenge_packet"
require "steam/packets/s2c_challenge_packet"
require "steam/packets/a2m_get_servers_batch2_packet"
require "steam/packets/m2a_server_batch_packet"
require "steam/packets/rcon/rcon_goldsrc_response"

# This class represents a packet used by the Source query protocol
class SteamPacket
  S2A_INFO_DETAILED_HEADER = 0x6D
  A2S_INFO_HEADER = 0x54
  S2A_INFO2_HEADER = 0x49
  A2A_PING_HEADER = 0x69
  A2A_ACK_HEADER = 0x6A
  A2S_PLAYER_HEADER = 0x55
  S2A_PLAYER_HEADER = 0x44
  A2S_RULES_HEADER = 0x56
  S2A_RULES_HEADER = 0x45
  A2A_SERVERQUERY_GETCHALLENGE_HEADER = 0x57
  S2C_CHALLENGE_HEADER = 0x41
  A2M_GET_SERVERS_BATCH2_HEADER = 0x31
  M2A_SERVER_BATCH_HEADER = 0x66
  S2C_CONNREJECT_HEADER = 0x39
  RCON_GOLDSRC_RESPONSE_HEADER = 0x6c

  # Creates a new packet object based on the header byte of the given raw data
  def self.create_packet(raw_data)
    header = raw_data[0].to_i;
    data = raw_data[1..-1];
    
    case header
      when SteamPacket::S2A_INFO_DETAILED_HEADER
        return S2A_INFO_DETAILED_HPacket.new(data)
      when SteamPacket::A2S_INFO_HEADER
        return A2S_INFO_Packet.new;
      when SteamPacket::S2A_INFO2_HEADER
        return S2A_INFO2_Packet.new(data)
      when SteamPacket::A2A_PING_HEADER
        return A2A_PING_Packet.new
      when SteamPacket::A2A_ACK_HEADER
        return A2A_ACK_Packet.new(data)
      when SteamPacket::A2S_PLAYER_HEADER
        return A2S_PLAYER_Packet.new
      when SteamPacket::S2A_PLAYER_HEADER
        return S2A_PLAYER_Packet.new(data)
      when SteamPacket::A2S_RULES_HEADER
        return A2S_RULES_Packet
      when SteamPacket::S2A_RULES_HEADER
        return S2A_RULES_Packet.new(data)
      when SteamPacket::A2S_SERVERQUERY_GETCHALLENGE_HEADER
        return A2S_SERVERQUERY_GETCHALLENGE_Packet.new
      when SteamPacket::S2C_CHALLENGE_HEADER
        return S2C_CHALLENGE_Packet.new(data)
      when SteamPacket::A2M_GET_SERVERS_BATCH2_HEADER
        return A2M_GET_SERVERS_BATCH2.new(data)
      when SteamPacket::M2A_SERVER_BATCH_HEADER
        return M2A_SERVER_BATCH_Packet.new(data)
      when SteamPacket::RCON_GOLDSRC_RESPONSE_HEADER
        return RCONGoldSrcResponse.new(data)
      else
        raise Exception.new("Unknown packet with header 0x#{header.to_s 16} received.")
    end
  end
  
  # Creates a new SteamPacket object with given header and content data
  def initialize(header_data, content_data = "")
    @content_data = ByteBuffer.new content_data
    @header_data = header_data
  end
  
  # Returns a packed string representing the packet's data
  #
  # TODO Has to automatically split packets greater than 1400 bytes
  def to_s
    packet_data = [0xFF, 0xFF, 0xFF].pack("ccc")
    
    unless @split_packet
      packet_data << [0xFF].pack("c")
    else
      packet_data << [0xFE].pack("c")
    end
    
    return packet_data << [@header_data, @content_data.array].pack("ca*")
  end    
end
