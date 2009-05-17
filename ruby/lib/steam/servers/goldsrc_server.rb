# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require "steam/servers/game_server"
require "steam/sockets/goldsrc_socket"

class GoldSrcServer < GameServer

  # Splits the player status obtained with +rcon status+
  def self.split_player_status(player_status)
    player_data = player_status.match(/# (\d+) "(.*)" (\d+) (.*)/).to_a[1..-1]
    player_data[3] = player_data[3].split
    player_data.flatten!
    player_data[0] = player_data[2]
    player_data.delete_at(2)
    player_data.delete_at(4)
    player_data
  end

  def initialize(ip_address, port_number = 27015, is_hltv = false)
    super port_number
    @socket = GoldSrcSocket.new ip_address, port_number, is_hltv
  end
  
  def rcon_auth(password)
    @rcon_password = password
    return true
  end
  
  def rcon_exec(command)
    return @socket.rcon_exec(@rcon_password, command).strip
  end
  
end