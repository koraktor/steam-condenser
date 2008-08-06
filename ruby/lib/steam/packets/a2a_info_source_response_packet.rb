autoload "A2A_INFO_ResponsePacket", "steam/packets/a2a_info_response_packet"

# The A2A_INFO_SourceResponsePacket class represents the response to a A2A_INFO
# request send to a Source server.
class A2A_INFO_SourceResponsePacket < A2A_INFO_ResponsePacket
  
  # Creates a A2A_INFO response object based on the data received.
  def initialize(data)
    super SteamPacket::A2A_INFO_SOURCE_RESPONSE_HEADER, data
    
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
    
    if extra_data_flag & 0x80
      @server_port = @content_data.get_short
    end
    
    if extra_data_flag & 0x40
      @tv_port = @content_data.get_short
      @tv_name = @content_data.get_string
    end   
    
    if extra_data_flag & 0x20
      @server_tags = @content_data.get_string
    end
  end
  
end
