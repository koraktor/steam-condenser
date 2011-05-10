# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'steam/packets/steam_packet'

# This packet class represents a A2S_SERVERQUERY_GETCHALLENGE request send to
# a game server
#
# It is used to retrieve a challenge number from the game server, which helps
# to identify the requesting client.
#
# @author Sebastian Staudt
# @see GameServer#update_challenge_number
class A2S_SERVERQUERY_GETCHALLENGE_Packet

  include SteamPacket

  # Creates a new A2S_SERVERQUERY_GETCHALLENGE request object
  def initialize
    super A2S_SERVERQUERY_GETCHALLENGE_HEADER
  end

end
