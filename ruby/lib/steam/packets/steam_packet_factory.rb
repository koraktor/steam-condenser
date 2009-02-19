# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "abstract_class"

class SteamPacketFactory
  
  include AbstractClass
  
  # Creates a new packet object based on the header byte of the given raw data
  def self.get_packet_from_data(raw_data)
    header = raw_data[0].to_i;
    data = raw_data[1..-1];
    
    case header
      when SteamPacket::S2A_INFO_DETAILED_HEADER
        return S2A_INFO_DETAILED_Packet.new(data)
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
      when SteamPacket::RCON_GOLDSRC_CHALLENGE_HEADER,
           SteamPacket::RCON_GOLDSRC_RESPONSE_HEADER
        return RCONGoldSrcResponse.new(data)
      else
        raise Exception.new("Unknown packet with header 0x#{header.to_s 16} received.")
    end
  end
  
  def self.reassemble_packet(split_packets, is_compressed = false, packet_checksum = 0)
    packet_data = split_packets.join ""
    
    if is_compressed
      if defined? BZ2
        packet_data = BZ2.uncompress(packet_data)
      else
        raise SteamCondenserException.new("You need to install the libbzip2 interface for Ruby.")
      end
      
      if Zlib.crc32(packet_data) != packet_checksum
        raise PacketFormatException.new("CRC32 checksum mismatch of uncompressed packet data.")
      end
    end
    
    packet_data = packet_data[4..-1]
    
    return self.get_packet_from_data(packet_data)
  end
  
end