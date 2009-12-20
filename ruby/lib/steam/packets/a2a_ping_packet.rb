# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require "steam/packets/steam_packet"

# The A2A_PING_Packet class represents a A2A_PING request send to the
# server.
class A2A_PING_Packet < SteamPacket
  
  # Creates a new A2A_PING request object
  def initialize
    super SteamPacket::A2A_PING_HEADER
  end
end