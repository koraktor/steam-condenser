# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "steam/packets/rcon/rcon_packet"

class RCONAuthResponse < RCONPacket
  
  def initialize(request_id)
    super request_id, RCONPacket::SERVERDATA_AUTH_RESPONSE, ""
  end
  
end