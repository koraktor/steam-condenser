# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2009-2011, Sebastian Staudt

require 'errors/steam_condenser_error'

# This error class indicates that the IP address your accessing the game server
# from has been banned by the server
#
# You or the server operator will have to unban your IP address on the server.
#
# @author Sebastian Staudt
# @see GameServer#rcon_auth
class RCONBanError < SteamCondenserError

  # Creates a new `RCONBanError` instance
  def initialize
    super 'You have been banned from this server.'
  end

end
