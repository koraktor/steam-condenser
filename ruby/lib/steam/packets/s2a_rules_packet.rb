# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'steam/packets/steam_packet'

# The S2A_RULES_Packet class represents the response to a A2S_RULES
# request send to the server.
class S2A_RULES_Packet < SteamPacket

  attr_reader :rules_hash

  # Creates a S2A_RULES response object based on the data received.
  def initialize(content_data)
    raise Exception.new('Wrong formatted A2A_RULES response packet.') if content_data.nil?

    super SteamPacket::S2A_RULES_HEADER, content_data

    rules_count = @content_data.short

    @rules_hash = {}

    rules_count.times do
      rule  = @content_data.string
      value = @content_data.string

      # This is a workaround for servers sending corrupt replies
      break if rule.empty? or value.empty?

      @rules_hash[rule] = value
    end
  end

end
