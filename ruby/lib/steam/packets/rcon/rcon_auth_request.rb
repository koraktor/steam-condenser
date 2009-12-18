# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt

require "steam/packets/rcon/rcon_packet"

class RCONAuthRequest < RCONPacket
  
  def initialize(request_id, rcon_password)
    super request_id, RCONPacket::SERVERDATA_AUTH, rcon_password
  end
  
end