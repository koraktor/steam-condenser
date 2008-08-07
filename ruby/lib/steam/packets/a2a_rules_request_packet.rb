# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "steam/packets/request_with_challenge"
require "steam/packets/steam_packet"

# Creates a new A2A_RULES request object including the challenge_number
class A2A_RULES_RequestPacket < SteamPacket
  
  include RequestWithChallenge
  
  # Creates a new A2A_RULES request object including the challenge_number
  def initialize(challenge_number = -1)
    super SteamPacket::A2A_RULES_REQUEST_HEADER, challenge_number
  end

end
