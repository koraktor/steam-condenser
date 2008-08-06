autoload "SteamSocket", "steam/sockets/steam_socket"

class MasterServerSocket < SteamSocket
  
  def get_reply
    self.receive_packet 1500
    
    if @buffer.get_long != -1
      raise Exception.new "Master query response has wrong packet header."
    end
    
    self.create_packet
  end
  
end