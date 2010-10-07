# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'steam/packets/request_with_challenge'
require 'steam/packets/steam_packet'

# Creates a new A2A_RULES request object including the challenge_number
class A2S_RULES_Packet

  include RequestWithChallenge
  include SteamPacket

  # Creates a new A2S_RULES request object including the challenge_number
  def initialize(challenge_number = -1)
    super A2S_RULES_HEADER, challenge_number
  end

end
