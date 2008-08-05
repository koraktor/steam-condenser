require "steam/servers/game_server"
require "steam/sockets/source_socket"

class SourceServer < GameServer
  
  def initialize(ip_address, port_number)
    super ip_address, port_number
    @socket = SourceSocket.new ip_address, port_number
  end
  
end