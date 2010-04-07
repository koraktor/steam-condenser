# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'steam/packets/request_with_challenge'
require 'steam/packets/steam_packet'

# The A2S_PLAYER_Packet class represents a A2S_PLAYER request send to the
# server.
class A2S_PLAYER_Packet < SteamPacket

  include RequestWithChallenge

  # Creates a new A2S_PLAYER request object including the challenge_number
  def initialize(challenge_number = -1)
    super SteamPacket::A2S_PLAYER_HEADER, challenge_number
  end

end
