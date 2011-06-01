# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'exceptions/steam_condenser_exception'

# This exception class indicates that you have not authenticated yet with the
# game server you're trying to send commands via RCON
#
# @author Sebastian Staudt
# @see GameServer#rcon_auth
# @see GameServer#rcon_exec
class RCONNoAuthException < SteamCondenserException

  # Creates a new `RCONNoAuthException` instance
  def initialize
    super 'Not authenticated yet.'
  end

end
