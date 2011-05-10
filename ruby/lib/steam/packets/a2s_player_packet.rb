# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'steam/packets/request_with_challenge'
require 'steam/packets/steam_packet'

# This packet class represents a A2S_PLAYER request send to a game server
#
# It is used to request the list of players currently playing on the server.
#
# This packet type requires the client to challenge the server in advance,
# which is done automatically if required.
#
# @author Sebastian Staudt
# @see GameServer#update_player_info
class A2S_PLAYER_Packet

  include SteamPacket
  include RequestWithChallenge

  # Creates a new A2S_PLAYER request object including the challenge number
  #
  # @param [Numeric] challenge_number The challenge number received from the
  #        server
  def initialize(challenge_number = -1)
    super A2S_PLAYER_HEADER, challenge_number
  end

end
