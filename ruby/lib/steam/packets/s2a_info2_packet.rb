# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require "steam/packets/s2a_info_base_packet"

# The S2A_INFO2_Packet class represents the response to a A2A_INFO
# request send to a Source server.
class S2A_INFO2_Packet < S2A_INFO_BasePacket
  
  # Creates a S2A_INFO2 response object based on the data received.
  def initialize(data)
    super SteamPacket::S2A_INFO2_HEADER, data
    
    @protocol_version = @content_data.get_byte
    @server_name = @content_data.get_string
    @map_name = @content_data.get_string
    @game_directory = @content_data.get_string
    @game_description = @content_data.get_string
    @app_id = @content_data.get_short
    @number_of_players = @content_data.get_byte
    @max_players = @content_data.get_byte
    @number_of_bots = @content_data.get_byte
    @dedicated = @content_data.get_byte.chr
    @operating_system = @content_data.get_byte.chr
    @password_needed = @content_data.get_byte == 1
    @secure = @content_data.get_byte == 1
    @game_version = @content_data.get_string
    extra_data_flag = @content_data.get_byte

    unless extra_data_flag & 0x80 == 0
      @server_port = @content_data.get_short
    end

    unless extra_data_flag & 0x10 == 0
      @server_id =  @content_data.get_long | (@content_data.get_long << 32)
    end

    unless extra_data_flag & 0x40 == 0
      @tv_port = @content_data.get_short
      @tv_name = @content_data.get_string
    end

    unless extra_data_flag & 0x20 == 0
      @server_tags = @content_data.get_string
    end
  end
  
end
