# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id: a2a_info_request_packet.rb 97 2008-08-07 07:25:49Z koraktor $

require "steam/packets/steam_packet"

# The A2S_INFO_Packet class represents a A2S_INFO request send to the
# server.
class A2S_INFO_Packet < SteamPacket
  
  # Creates a new A2S_INFO request object
	def initialize
		super SteamPacket::A2S_INFO_HEADER, "Source Engine Query"
	end
end