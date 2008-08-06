# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.

require "steam/packets/master_server_query_request_packet"
require "steam/sockets/master_server_socket"

class MasterServer
  
  GOLDSRC_MASTER_SERVER = "hl1master.steampowered.com", 27010
  SOURCE_MASTER_SERVER = "hl2master.steampowered.com", 27011
  
  def initialize(master_server_address)
    @socket = MasterServerSocket.new *master_server_address
    @server_array = Array.new
  end
  
  def get_servers(region_code = MasterServerQueryRequestPacket::REGION_ALL, filters = "")
    if @server_array.empty?
      self.update_servers region_code, filters
    end
    
    return @server_array
  end
  
  def update_servers(region_code, filters)
    finished = false
    current_server = "0.0.0.0:0"
    
    begin
      @socket.send MasterServerQueryRequestPacket.new(region_code, current_server, filters)
      servers = @socket.get_reply.get_servers
      
      servers.each do |current_server|
        if current_server == "0.0.0.0:0"
          finished = true
        else
          @server_array << current_server.split(":")
        end
      end
    end while !finished
  end
  
end