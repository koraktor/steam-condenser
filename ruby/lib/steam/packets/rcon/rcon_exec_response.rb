# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'steam/packets/rcon/rcon_packet'

class RCONExecResponse

  include RCONPacket

  def initialize(request_id, command_response)
    super request_id, SERVERDATA_RESPONSE_VALUE, command_response
  end

  def response
    @content_data.string[0..-3]
  end

end
