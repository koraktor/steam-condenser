# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "steam/packets/rcon/rcon_packet"

class RCONExecResponse < RCONPacket
  
  def initialize(request_id, command_response)
    super request_id, RCONPacket::SERVERDATA_RESPONSE_VALUE, command_response
  end
  
  def get_response
    return @content_data.array
  end
  
end