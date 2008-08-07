# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "steam/servers/game_server"
require "steam/sockets/source_socket"

class SourceServer < GameServer
  
  def initialize(ip_address, port_number = 27015)
    super port_number
    @socket = SourceSocket.new ip_address, port_number
  end
  
end