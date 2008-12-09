# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "steam/packets/steam_packet"

# The S2A_PLAYER_Packet class represents the response to a A2S_PLAYER
# request send to the server.
class S2A_PLAYER_Packet < SteamPacket
  
  # Creates a S2A_PLAYER response object based on the data received.
  def initialize(content_data)
    if content_data == nil
      raise Exception.new("Wrong formatted S2A_PLAYER packet.")
    end
    
    super SteamPacket::S2A_PLAYER_HEADER, content_data
    
    @player_array = Array.new @content_data.get_byte
    
    while @content_data.remaining > 0
      player_data = @content_data.get_byte, @content_data.get_string, @content_data.get_long, @content_data.get_float
      @player_array[player_data[0]] = SteamPlayer.new *player_data[0..3]
    end
  end
  
  # Returns the array containing SteamPlayer objects for all players on the
  # server
  def get_player_array
    return @player_array
  end
end
