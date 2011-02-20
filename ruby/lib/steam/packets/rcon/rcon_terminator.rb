# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'steam/packets/rcon/rcon_packet'

# This class is used to determine the end of a RCON response from Source
# servers. Packets of this type are sent after the actual RCON command and the
# empty response packet from the server will indicate the end of the response.
class RCONTerminator

  include RCONPacket

  # Creates a new RCONTerminator instance for the given request ID
  def initialize(request_id)
    super request_id, RCONPacket::SERVERDATA_RESPONSE_VALUE, nil
  end

end
