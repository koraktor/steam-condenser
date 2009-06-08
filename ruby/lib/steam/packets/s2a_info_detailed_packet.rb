# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require "steam/packets/s2a_info_base_packet"

# The S2A_INFO_DETAILED_Packet class represents the response to a A2S_INFO
# request send to a GoldSrc server.
# This is deprecated by 10/24/2008 for GoldSrc servers. They use the same
# format as Source servers (S2A_INFO2) now.
class S2A_INFO_DETAILED_Packet < S2A_INFO_BasePacket
  
  # Creates a S2A_INFO_DETAILED response object based on the data received.
  def initialize(data)
    super SteamPacket::S2A_INFO_DETAILED_HEADER, data

    @game_ip = @content_data.get_string
    @server_name = @content_data.get_string
    @map_name = @content_data.get_string
    @game_directory = @content_data.get_string
    @game_description = @content_data.get_string
    @number_of_players = @content_data.get_byte
    @max_players = @content_data.get_byte
    @network_version = @content_data.get_byte
    @dedicated = @content_data.get_byte.chr
    @operating_system = @content_data.get_byte.chr
    @password_needed = @content_data.get_byte == 1
    @is_mod = @content_data.get_byte == 1
    
    if @is_mod
      @mod_info = Hash.new
      @mod_info["url_info"] = @content_data.get_string
      @mod_info["url_dl"] = @content_data.get_string
      @content_data.get_byte
      if @content_data.remaining == 12
        @mod_info["mod_version"] = @content_data.get_long
        @mod_info["mod_size"] = @content_data.get_long
        @mod_info["sv_only"] = @content_data.get_byte == 1
        @mod_info["cl_dll"] = @content_data.get_byte == 1
        @secure = @content_data.get_byte == 1
        @number_of_bots = @content_data.get_byte
      end
    else
      @secure = @content_data.get_byte == 1
      @number_of_bots = @content_data.get_byte
    end
  end
  
end
