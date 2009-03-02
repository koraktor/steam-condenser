# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt
#
# $Id$

require "steam/packets/steam_packet"
require "steam/servers/master_server"

class A2M_GET_SERVERS_BATCH2_Packet < SteamPacket
  
  # Creates a master server request, filtering by the given paramters.
  def initialize(region_code = MasterServer::REGION_ALL, start_ip = "0.0.0.0:0", filter = "")
    super SteamPacket::A2M_GET_SERVERS_BATCH2_HEADER
    
    @filter = filter;
    @region_code = region_code;
    @start_ip = start_ip;
  end
  
  def to_s
    return [@header_data, @region_code, @start_ip, @filter].pack("c2Z*Z*")
  end
end