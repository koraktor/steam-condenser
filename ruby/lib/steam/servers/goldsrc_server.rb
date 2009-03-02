# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt
#
# $Id$

require "steam/servers/game_server"
require "steam/sockets/goldsrc_socket"

class GoldSrcServer < GameServer
  
  def initialize(ip_address, port_number = 27015, is_hltv = false)
    super port_number
    @socket = GoldSrcSocket.new ip_address, port_number, is_hltv
  end
  
  def rcon_auth(password)
    @rcon_password = password
    return true
  end
  
  def rcon_exec(command)
    return @socket.rcon_exec(@rcon_password, command)
  end
  
end