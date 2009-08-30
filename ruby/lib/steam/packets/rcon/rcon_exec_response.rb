# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require "steam/packets/rcon/rcon_packet"

class RCONExecResponse < RCONPacket

  def initialize(request_id, command_response)
    super request_id, RCONPacket::SERVERDATA_RESPONSE_VALUE, command_response
  end

  def get_response
    @content_data.array[0..-3]
  end

end
