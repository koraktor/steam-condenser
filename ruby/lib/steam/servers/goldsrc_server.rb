require "steam/servers/game_server"
require "steam/sockets/goldsrc_socket"

class GoldSrcServer < GameServer
  
  def initialize(ip_address, port_number)
    super ip_address, port_number
    @socket = GoldSrcSocket.new ip_address, port_number
  end
  
end