# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

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
  A2S_SERVERQUERY_GETCHALLENGE_HEADER = 0x57
  S2C_CHALLENGE_HEADER = 0x41
  A2M_GET_SERVERS_BATCH2_HEADER = 0x31
  M2A_SERVER_BATCH_HEADER = 0x66
  S2C_CONNREJECT_HEADER = 0x39
  RCON_GOLDSRC_CHALLENGE_HEADER = 0x63
  RCON_GOLDSRC_NO_CHALLENGE_HEADER = 0x39
  RCON_GOLDSRC_RESPONSE_HEADER = 0x6c

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
