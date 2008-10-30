# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id: master_server_query_request_packet.rb 103 2008-08-10 19:58:11Z koraktor $

require "steam/packets/steam_packet"

class A2M_GET_SERVERS_BATCH2_Packet < SteamPacket
  
  REGION_US_EAST_COAST = 0x00
  REGION_US_WEST_COAST = 0x01
  REGION_SOUTH_AMERICA = 0x02
  REGION_EUROPE = 0x03
  REGION_ASIA = 0x04
  REGION_AUSTRALIA = 0x05
  REGION_MIDDLE_EAST = 0x06
  REGION_AFRICA = 0x07
  REGION_ALL = 0x00
  
  # Creates a master server request, filtering by the given paramters.
  def initialize(region_code = A2M_GET_SERVERS_BATCH2_Packet::REGION_ALL, start_ip = "0.0.0.0:0", filter = "")
    super SteamPacket::A2M_GET_SERVERS_BATCH2_HEADER
    
    @filter = filter;
    @region_code = region_code;
    @start_ip = start_ip;
  end
  
  def to_s
    return [@header_data, @region_code, @start_ip, @filter].pack("c2Z*Z*")
  end
end