# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "steam/packets/a2m_get_servers_batch2_packet"
require "steam/sockets/master_server_socket"

class MasterServer
  
  GOLDSRC_MASTER_SERVER = "hl1master.steampowered.com", 27010
  SOURCE_MASTER_SERVER = "hl2master.steampowered.com", 27011
  
  def initialize(master_server_address)
    @socket = MasterServerSocket.new *master_server_address
    @server_array = Array.new
  end
  
  def get_servers(region_code = A2M_GET_SERVERS_BATCH2_Packet::REGION_ALL, filters = "")
    if @server_array.empty?
      self.update_servers region_code, filters
    end
    
    return @server_array
  end
  
  def update_servers(region_code, filters)
    finished = false
    current_server = "0.0.0.0:0"
    
    begin
      @socket.send A2M_GET_SERVERS_BATCH2_Packet.new(region_code, current_server, filters)
      servers = @socket.get_reply.get_servers
      
      servers.each do |server|
        if server == "0.0.0.0:0"
          finished = true
        else
          current_server = server
          @server_array << server.split(":")
        end
      end
    end while !finished
  end
  
end