# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require "steam/packets/steam_packet"

# The S2A_PLAYER_Packet class represents the response to a A2S_PLAYER
# request send to the server.
class S2A_PLAYER_Packet < SteamPacket

  attr_reader :player_hash
  
  # Creates a S2A_PLAYER response object based on the data received.
  def initialize(content_data)
    if content_data == nil
      raise Exception.new("Wrong formatted S2A_PLAYER packet.")
    end
    
    super SteamPacket::S2A_PLAYER_HEADER, content_data

    # Ignore player count
    @content_data.get_byte
    @player_hash = {}

    while @content_data.remaining > 0
      player_data = @content_data.get_byte, @content_data.get_string, @content_data.get_long, @content_data.get_float
      @player_hash[player_data[1]] = SteamPlayer.new(*player_data[0..3])
    end
  end

end
