# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'steam/packets/rcon/rcon_packet'

# This packet class represents a SERVERDATA_EXECCOMMAND packet sent to a
# Source server
#
# It is used to request a command execution on the server.
#
# @author Sebastian Staudt
# @see SourceServer#rcon_exec
class RCONExecRequest

  include RCONPacket

  # Creates a RCON command execution request for the given request ID and
  # command
  #
  # @param [Fixnum] request_id The request ID of the RCON connection
  # @param [String] rcon_command The command to execute on the server
  def initialize(request_id, rcon_command)
    super request_id, SERVERDATA_EXECCOMMAND, rcon_command
  end

end
