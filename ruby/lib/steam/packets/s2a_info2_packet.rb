# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'steam/packets/s2a_info_base_packet'

# The S2A_INFO2_Packet class represents the response to a A2A_INFO
# request send to a Source server.
class S2A_INFO2_Packet < S2A_INFO_BasePacket

  # Creates a S2A_INFO2 response object based on the data received.
  def initialize(data)
    super SteamPacket::S2A_INFO2_HEADER, data

    @protocol_version = @content_data.byte
    @server_name = @content_data.cstring
    @map_name = @content_data.cstring
    @game_directory = @content_data.cstring
    @game_description = @content_data.cstring
    @app_id = @content_data.short
    @number_of_players = @content_data.byte
    @max_players = @content_data.byte
    @number_of_bots = @content_data.byte
    @dedicated = @content_data.byte.chr
    @operating_system = @content_data.byte.chr
    @password_needed = @content_data.byte == 1
    @secure = @content_data.byte == 1
    @game_version = @content_data.cstring
    extra_data_flag = @content_data.byte

    @server_port = @content_data.short unless extra_data_flag & 0x80 == 0

    unless extra_data_flag & 0x10 == 0
      @server_id =  @content_data.long | (@content_data.long << 32)
    end

    unless extra_data_flag & 0x40 == 0
      @tv_port = @content_data.short
      @tv_name = @content_data.cstring
    end

    @server_tags = @content_data.cstring unless extra_data_flag & 0x20 == 0

    generate_info_hash
  end

end
