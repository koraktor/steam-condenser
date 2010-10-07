# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'steam/packets/rcon/rcon_packet'

class RCONAuthRequest

  include RCONPacket

  def initialize(request_id, rcon_password)
    super request_id, SERVERDATA_AUTH, rcon_password
  end

end
