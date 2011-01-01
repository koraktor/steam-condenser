# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'exceptions/steam_condenser_exception'
require 'steam/packets/s2a_info_detailed_packet'
require 'steam/packets/a2s_info_packet'
require 'steam/packets/s2a_info2_packet'
require 'steam/packets/a2s_player_packet'
require 'steam/packets/s2a_player_packet'
require 'steam/packets/a2s_rules_packet'
require 'steam/packets/s2a_rules_packet'
require 'steam/packets/a2s_serverquery_getchallenge_packet'
require 'steam/packets/s2c_challenge_packet'
require 'steam/packets/a2m_get_servers_batch2_packet'
require 'steam/packets/m2a_server_batch_packet'
require 'steam/packets/m2c_isvalidmd5_packet'
require 'steam/packets/m2s_requestrestart_packet'
require 'steam/packets/s2a_logstring_packet'
require 'steam/packets/rcon/rcon_goldsrc_response'

module SteamPacketFactory

  # Creates a new packet object based on the header byte of the given raw data
  def self.packet_from_data(raw_data)
    header = raw_data[0].ord
    data = raw_data[1..-1]

    case header
      when SteamPacket::S2A_INFO_DETAILED_HEADER
        return S2A_INFO_DETAILED_Packet.new(data)
      when SteamPacket::A2S_INFO_HEADER
        return A2S_INFO_Packet.new
      when SteamPacket::S2A_INFO2_HEADER
        return S2A_INFO2_Packet.new(data)
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
        return A2M_GET_SERVERS_BATCH2_Packet.new(data)
      when SteamPacket::M2A_SERVER_BATCH_HEADER
        return M2A_SERVER_BATCH_Packet.new(data)
      when SteamPacket::M2C_ISVALIDMD5_HEADER
        return M2C_ISVALIDMD5_Packet.new(data)
      when SteamPacket::M2S_REQUESTRESTART_HEADER
        return M2S_REQUESTRESTART_Packet.new(data)
      when SteamPacket::RCON_GOLDSRC_CHALLENGE_HEADER,
           SteamPacket::RCON_GOLDSRC_NO_CHALLENGE_HEADER,
           SteamPacket::RCON_GOLDSRC_RESPONSE_HEADER
        return RCONGoldSrcResponse.new(data)
      when SteamPacket::S2A_LOGSTRING_HEADER
        return S2A_LOGSTRING_Packet.new(data)
      else
        raise SteamCondenserException.new("Unknown packet with header 0x#{header.to_s(16)} received.")
    end
  end

  def self.reassemble_packet(split_packets, is_compressed = false, packet_checksum = 0)
    packet_data = split_packets.join ''

    if is_compressed
      require 'zlib'

      begin
        require 'bz2'
        packet_data = BZ2.uncompress(packet_data)
      rescue LoadError
        raise SteamCondenserException.new('You need to install the libbzip2 interface for Ruby.')
      end

      unless Zlib.crc32(packet_data) == packet_checksum
        raise PacketFormatException.new('CRC32 checksum mismatch of uncompressed packet data.')
      end
    end

    packet_from_data packet_data[4..-1]
  end

end
