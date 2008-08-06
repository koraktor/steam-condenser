# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.

autoload "SteamPacket", "steam/packets/steam_packet"
require "enumerator"

# The A2A_RULES_ResponsePacket class represents the response to a A2A_RULES
# request send to the server.
class A2A_RULES_ResponsePacket < SteamPacket
  
  # Creates a A2A_RULES response object based on the data received.
  def initialize(content_data)
    if content_data == nil
      raise Exception.new("Wrong formatted A2A_RULES response packet.")
    end
    
    super SteamPacket::A2A_RULES_RESPONSE_HEADER, content_data
    
    @rules_hash = Hash.new @content_data.get_short

    while @content_data.remaining > 0
      @rules_hash[@content_data.get_string] = @content_data.get_string
    end
  end

  # Returns the hash containing the server rules
  def get_rules_hash
    return @rules_hash
  end
end
