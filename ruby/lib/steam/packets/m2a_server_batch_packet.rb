# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id: master_server_query_response_packet.rb 97 2008-08-07 07:25:49Z koraktor $

require "steam/packets/steam_packet"

class M2A_SERVER_BATCH_Packet < SteamPacket
  
  def initialize(data)
    super SteamPacket::M2A_SERVER_BATCH_HEADER, data
    
    if(@content_data.get_byte != 10)
      raise PacketFormatException.new("Master query response is missing additional 0x0A byte.")
    end
    
    @server_array = Array.new
    
    begin
      first_octet = @content_data.get_byte
      second_octet = @content_data.get_byte
      third_octet = @content_data.get_byte
      fourth_octet = @content_data.get_byte
      port_number = @content_data.get_short
      port_number = ((port_number & 0xFF) << 8) + (port_number >> 8)
      
      @server_array << "#{first_octet}.#{second_octet}.#{third_octet}.#{fourth_octet}:#{port_number}"
    end while @content_data.remaining > 0
  end
  
  def get_servers
    return @server_array
  end
  
end