# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.

require "abstract_class"
require "steam/packets/steam_packet"

# The S2A_INFO_BasePacket class represents the response to a A2S_INFO
# request send to the server.
class S2A_INFO_BasePacket < SteamPacket
  
  include AbstractClass
  
  # Creates a S2A_INFO[...] response object based on the data received.
  def initialize(header, data)
    super header, data
  end
  
  # Returns the hash containing information on the server
  public
  def get_info_hash
    return Hash[
      *instance_variables.map { |var|
        if var != "@content_data" && var != "@header_data"
          [var[1..-1], instance_variable_get(var)]
        end
      }.compact.flatten
    ]
  end
end
