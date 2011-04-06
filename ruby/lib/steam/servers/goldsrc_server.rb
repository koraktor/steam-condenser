# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'steam/servers/game_server'
require 'steam/sockets/goldsrc_socket'

class GoldSrcServer

  include GameServer

  def initialize(ip_address, port_number = 27015, is_hltv = false)
    super port_number
    @socket = GoldSrcSocket.new ip_address, port_number, is_hltv
  end

  def rcon_auth(password)
    @rcon_password = password
    true
  end

  def rcon_exec(command)
    @socket.rcon_exec(@rcon_password, command).strip
  end

end
