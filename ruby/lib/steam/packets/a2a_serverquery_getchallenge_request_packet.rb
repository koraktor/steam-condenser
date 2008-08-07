# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "steam/packets/steam_packet"

# The A2A_SERVERQUERY_GETCHALLENGE_RequestPacket class represents a
# A2A_SERVERQUERY_GETCHALLENGE request send to the server.
class A2A_SERVERQUERY_GETCHALLENGE_RequestPacket < SteamPacket
  
  # Creates a new A2A_SERVERQUERY_GETCHALLENGE request object
  def initialize
    super SteamPacket::A2A_SERVERQUERY_GETCHALLENGE_REQUEST_HEADER
  end
end
