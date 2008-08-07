# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "steam/packets/steam_packet"

# The A2A_PING_ResponsePacket class represents the response to a A2A_PING
# request send to the server.
class A2A_PING_ResponsePacket < SteamPacket
  
  # Creates a A2A_PING response object based on the data received.
	def initialize(data)
		if data != "\0" && data != "00000000000000\0"
			raise Exception.new("Wrong formatted A2A_PING Response Packet.")
		end
		super SteamPacket::A2A_PING_RESPONSE_HEADER, data
	end
end
