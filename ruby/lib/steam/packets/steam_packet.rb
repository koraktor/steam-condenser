# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.

autoload "A2A_INFO_GoldSrcResponsePacket", "steam/packets/a2a_info_goldsrc_response_packet"
autoload "A2A_INFO_RequestPacket", "steam/packets/a2a_info_request_packet"
autoload "A2A_INFO_SourceResponsePacket", "steam/packets/a2a_info_source_response_packet"
autoload "A2A_PING_RequestPacket", "steam/packets/a2a_ping_request_packet"
autoload "A2A_PING_ResponsePacket", "steam/packets/a2a_ping_response_packet"
autoload "A2A_PLAYER_RequestPacket", "steam/packets/a2a_player_request_packet"
autoload "A2A_PLAYER_ResponsePacket", "steam/packets/a2a_player_response_packet"
autoload "A2A_RULES_RequestPacket", "steam/packets/a2a_rules_request_packet"
autoload "A2A_RULES_ResponsePacket", "steam/packets/a2a_rules_response_packet"
autoload "A2A_SERVERQUERY_GETCHALLENGE_RequestPacket", "steam/packets/a2a_serverquery_getchallenge_request_packet"
autoload "A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket", "steam/packets/a2a_serverquery_getchallenge_response_packet"
autoload "MasterServerQueryRequestPacket", "steam/packets/master_server_query_request_packet"
autoload "MasterServerQueryResponsePacket", "steam/packets/master_server_query_response_packet"

# This class represents a packet used by the Source query protocol
class SteamPacket
  A2A_INFO_GOLDSRC_RESPONSE_HEADER = 0x6D
  A2A_INFO_REQUEST_HEADER = 0x54
  A2A_INFO_SOURCE_RESPONSE_HEADER = 0x49
  A2A_PING_REQUEST_HEADER = 0x69
  A2A_PING_RESPONSE_HEADER = 0x6A
  A2A_PLAYER_REQUEST_HEADER = 0x55
  A2A_PLAYER_RESPONSE_HEADER = 0x44
  A2A_RULES_REQUEST_HEADER = 0x56
  A2A_RULES_RESPONSE_HEADER = 0x45
  A2A_SERVERQUERY_GETCHALLENGE_REQUEST_HEADER = 0x57
  A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER = 0x41
  MASTER_SERVER_QUERY_REQUEST_HEADER = 0x31
  MASTER_SERVER_QUERY_RESPONSE_HEADER = 0x66

  # Creates a new packet object based on the header byte of the given raw data
  def self.create_packet(raw_data)
    header = raw_data[0].to_i;
    data = raw_data[1..-1];
    
    case header
      when SteamPacket::A2A_INFO_GOLDSRC_RESPONSE_HEADER
        return A2A_INFO_GoldSrcResponsePacket.new(data)
      when SteamPacket::A2A_INFO_REQUEST_HEADER
        return A2A_INFO_RequestPacket.new;
      when SteamPacket::A2A_INFO_SOURCE_RESPONSE_HEADER
        return A2A_INFO_SourceResponsePacket.new(data)
      when SteamPacket::A2A_PING_REQUEST_HEADER
        return A2A_PING_RequestPacket.new
      when SteamPacket::A2A_PING_RESPONSE_HEADER
        return A2A_PING_ResponsePacket.new(data)
      when SteamPacket::A2A_PLAYER_REQUEST_HEADER
        return A2A_PLAYER_ResponsePacket.new
      when SteamPacket::A2A_PLAYER_RESPONSE_HEADER
        return A2A_PLAYER_ResponsePacket.new(data)
      when SteamPacket::A2A_RULES_REQUEST_HEADER
        return A2A_RULES_RequestPacket
      when SteamPacket::A2A_RULES_RESPONSE_HEADER
        return A2A_RULES_ResponsePacket.new(data)
      when SteamPacket::A2A_SERVERQUERY_GETCHALLENGE_REQUEST_HEADER
        return A2A_SERVERQUERY_GETCHALLENGE_RequestPacket.new
      when SteamPacket::A2A_SERVERQUERY_GETCHALLENGE_RESPONSE_HEADER
        return A2A_SERVERQUERY_GETCHALLENGE_ResponsePacket.new(data)
      when SteamPacket::MASTER_SERVER_QUERY_REQUEST_HEADER
        return MasterServerQueryRequestPacket.new(data)
      when SteamPacket::MASTER_SERVER_QUERY_RESPONSE_HEADER
        return MasterServerQueryResponsePacket.new(data)
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
