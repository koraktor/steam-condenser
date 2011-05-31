# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'steam/packets/rcon/rcon_packet'

# This packet class represents a special SERVERDATA_RESPONSE_VALUE packet which
# is sent to the server
#
# It is used to determine the end of a RCON response from Source servers.
# Packets of this type are sent after the actual RCON command and the empty
# response packet from the server will indicate the end of the response.
#
# @author Sebastian Staudt
# @see SourceServer#rcon_exec
class RCONTerminator

  include RCONPacket

  # Creates a new RCON terminator packet instance for the given request ID
  #
  # @param [Fixnum] request_id The request ID for the current RCON
  #        communication
  def initialize(request_id)
    super request_id, SERVERDATA_RESPONSE_VALUE, nil
  end

end
