# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'steam/packets/steam_packet'

# The S2A_PLAYER_Packet class represents the response to a A2S_PLAYER
# request send to the server.
class S2A_PLAYER_Packet

  include SteamPacket

  attr_reader :player_hash

  # Creates a S2A_PLAYER response object based on the data received.
  def initialize(content_data)
    raise Exception.new('Wrong formatted S2A_PLAYER packet.') if content_data.nil?

    super S2A_PLAYER_HEADER, content_data

    # Ignore player count
    @content_data.byte
    @player_hash = {}

    while @content_data.remaining > 0
      player_data = @content_data.byte, @content_data.cstring, @content_data.signed_long, @content_data.float
      @player_hash[player_data[1]] = SteamPlayer.new(*player_data[0..3])
    end
  end

end
