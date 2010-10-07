# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'steam/packets/rcon/rcon_packet'

class RCONAuthResponse

  include RCONPacket

  def initialize(request_id)
    super request_id, SERVERDATA_AUTH_RESPONSE, ''
  end

end
