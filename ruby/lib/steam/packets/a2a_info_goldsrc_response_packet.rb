autoload "A2A_INFO_ResponsePacket", "steam/packets/a2a_info_response_packet"

# The A2A_INFO_GoldSrcResponsePacket class represents the response to a A2A_INFO
# request send to a GoldSrc server.
class A2A_INFO_GoldSrcResponsePacket < A2A_INFO_ResponsePacket
  
  # Creates a A2A_INFO response object based on the data received.
  def initialize(data)
    super SteamPacket::A2A_INFO_GOLDSRC_RESPONSE_HEADER, data

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
      @mod_info["mod_version"] = @content_data.get_long
      @mod_info["mod_size"] = @content_data.get_long
      @mod_info["sv_only"] = @content_data.get_byte == 1
      @mod_info["cl_dll"] = @content_data.get_byte == 1
    end
    
    @secure = @content_data.get_byte == 1
    @number_of_bots = @content_data.get_byte
  end
  
end
