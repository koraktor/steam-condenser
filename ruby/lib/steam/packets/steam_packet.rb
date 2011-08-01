# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'core_ext/stringio'

# This module implements the basic functionality used by most of the packets
# used in communication with master, Source or GoldSrc servers.
#
# @author Sebastian Staudt
# @see SteamPacketFactory
module SteamPacket

  A2M_GET_SERVERS_BATCH2_HEADER = 0x31
  A2S_INFO_HEADER = 0x54
  A2S_PLAYER_HEADER = 0x55
  A2S_RULES_HEADER = 0x56
  A2S_SERVERQUERY_GETCHALLENGE_HEADER = 0x57
  C2M_CHECKMD5_HEADER = 0x4D
  M2A_SERVER_BATCH_HEADER = 0x66
  M2C_ISVALIDMD5_HEADER = 0x4E
  M2S_REQUESTRESTART_HEADER = 0x4F
  RCON_GOLDSRC_CHALLENGE_HEADER = 0x63
  RCON_GOLDSRC_NO_CHALLENGE_HEADER = 0x39
  RCON_GOLDSRC_RESPONSE_HEADER = 0x6c
  S2A_INFO_DETAILED_HEADER = 0x6D
  S2A_INFO2_HEADER = 0x49
  S2A_LOGSTRING_HEADER = 0x52
  S2A_PLAYER_HEADER = 0x44
  S2A_RULES_HEADER = 0x45
  S2C_CONNREJECT_HEADER = 0x39
  S2C_CHALLENGE_HEADER = 0x41
  S2M_HEARTBEAT2_HEADER = 0x30

  # Creates a new packet object based on the given data
  #
  # @param [Fixnum] header_data The packet header
  # @param [String] content_data The raw data of the packet
  def initialize(header_data, content_data = '')
    @content_data = StringIO.new content_data.to_s
    @header_data  = header_data
  end

  # Returns the raw data representing this packet
  #
  # @return [String] A string containing the raw data of this request packet
  def to_s
    [0xFF, 0xFF, 0xFF, 0xFF, @header_data, @content_data.string].pack('c5a*')
  end

end
