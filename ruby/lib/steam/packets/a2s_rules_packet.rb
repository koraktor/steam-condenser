# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'steam/packets/request_with_challenge'
require 'steam/packets/steam_packet'

# This packet class represents a A2S_RULES request send to a game server
#
# The game server will return a list of currently active game rules, e.g.
# `mp_friendlyfire = 1`.
#
# This packet type requires the client to challenge the server in advance,
# which is done automatically if required.
#
# @author Sebastian Staudt
# @see GameServer#update_rules_info
class A2S_RULES_Packet

  include SteamPacket
  include RequestWithChallenge

  # Creates a new A2S_RULES request object including the challenge number
  #
  # @param [Numeric] challenge_number The challenge number received from the
  #        server
  def initialize(challenge_number = -1)
    super A2S_RULES_HEADER, challenge_number
  end

end
