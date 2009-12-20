# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require "steam/packets/steam_packet"

class RCONGoldSrcResponse < SteamPacket
  
  def initialize(command_response)
    super SteamPacket::RCON_GOLDSRC_RESPONSE_HEADER, command_response
  end
  
  def get_response
    return @content_data.array[0..-3]
  end
  
end