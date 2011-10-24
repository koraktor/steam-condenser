# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'steam/packets/steam_packet'

# This class represents a S2A_RULES response sent by a game server
#
# It is used to transfer a list of server rules (a.k.a. CVARs) with their
# active values.
#
# @author Sebastian Staudt
# @see GameServer#update_rules_info
class S2A_RULES_Packet

  include SteamPacket

  # Returns the list of server rules (a.k.a. CVars) with the current values
  #
  # @return [Hash<String, String>] A list of server rules
  attr_reader :rules_hash

  # Creates a new S2A_RULES response object based on the given data
  #
  # @param [String] content_data The raw packet data sent by the server
  def initialize(content_data)
    if content_data.nil?
      raise PacketFormatError, 'Wrong formatted S2A_RULES response packet.'
    end

    super SteamPacket::S2A_RULES_HEADER, content_data

    rules_count = @content_data.short

    @rules_hash = {}

    rules_count.times do
      rule  = @content_data.cstring
      value = @content_data.cstring

      break if rule.empty?

      @rules_hash[rule] = value
    end
  end

end
