# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'steam/packets/rcon/rcon_packet'

# This packet class represents a SERVERDATA_AUTH request sent to a Source
# server
#
# It is used to authenticate the client for RCON communication.
#
# @author Sebastian Staudt
# @see SourceServer#rcon_auth
class RCONAuthRequest

  include RCONPacket

  # Creates a RCON authentication request for the given request ID and RCON
  # password
  #
  # @param [Fixnum] request_id The request ID of the RCON connection
  # @param [String] rcon_password The RCON password of the server
  def initialize(request_id, rcon_password)
    super request_id, SERVERDATA_AUTH, rcon_password
  end

end
