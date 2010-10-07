# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'steam/packets/steam_packet'

# The A2S_SERVERQUERY_GETCHALLENGE_Packet class represents a
# A2S_SERVERQUERY_GETCHALLENGE request send to the server.
class A2S_SERVERQUERY_GETCHALLENGE_Packet

  include SteamPacket

  # Creates a new A2S_SERVERQUERY_GETCHALLENGE request object
  def initialize
    super A2S_SERVERQUERY_GETCHALLENGE_HEADER
  end

end
