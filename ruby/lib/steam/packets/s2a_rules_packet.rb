# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require "steam/packets/steam_packet"

# The S2A_RULES_Packet class represents the response to a A2S_RULES
# request send to the server.
class S2A_RULES_Packet < SteamPacket

  # Creates a S2A_RULES response object based on the data received.
  def initialize(content_data)
    if content_data == nil
      raise Exception.new("Wrong formatted A2A_RULES response packet.")
    end

    super SteamPacket::S2A_RULES_HEADER, content_data

    rules_count = @content_data.get_short

    @rules_hash = {}

    rules_count.times do
      rule  = @content_data.get_string
      value = @content_data.get_string

      # This is a workaround for servers sending corrupt replies
      break if rule.empty? or value.empty?

      @rules_hash[rule] = value
      puts "#{rule} = #{value}"
    end
  end

  # Returns the hash containing the server rules
  def get_rules_hash
    return @rules_hash
  end
end
