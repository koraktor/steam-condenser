# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'errors/steam_condenser_error'

# This error class indicates that you have not authenticated yet with the game
# server you're trying to send commands via RCON
#
# @author Sebastian Staudt
# @see GameServer#rcon_auth
# @see GameServer#rcon_exec
class RCONNoAuthError < SteamCondenserError

  # Creates a new `RCONNoAuthError` instance
  def initialize
    super 'Not authenticated yet.'
  end

end
