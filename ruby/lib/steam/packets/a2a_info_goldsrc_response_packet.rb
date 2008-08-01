autoload "ByteBuffer", "byte_buffer"
autoload "A2A_INFO_ResponsePacket", "steam/packets/a2a_info_response_packet"

# The A2A_INFO_GoldSrcResponsePacket class represents the response to a A2A_INFO
# request send to a GoldSrc server.
class A2A_INFO_GoldSrcResponsePacket < A2A_INFO_ResponsePacket
  
  # Creates a A2A_INFO response object based on the data received.
  def initialize(data)
    super SteamPacket::A2A_INFO_GOLDSRC_RESPONSE_HEADER, data

    byte_buffer = ByteBuffer.new data
    
    @game_ip = byte_buffer.get_string
    @server_name = byte_buffer.get_string
    @map_name = byte_buffer.get_string
    @game_directory = byte_buffer.get_string
    @game_description = byte_buffer.get_string
    @number_of_players = byte_buffer.get_byte
    @max_players = byte_buffer.get_byte
    @network_version = byte_buffer.get_byte
    @dedicated = byte_buffer.get_byte.chr
    @operating_system = byte_buffer.get_byte.chr
    @password_needed = byte_buffer.get_byte == 1
    @is_mod = byte_buffer.get_byte == 1
    
    if @is_mod
      @mod_info = Hash.new
      @mod_info["url_info"] = byte_buffer.get_string
      @mod_info["url_dl"] = byte_buffer.get_string
      byte_buffer.get_byte
      @mod_info["mod_version"] = byte_buffer.get_long
      @mod_info["mod_size"] = byte_buffer.get_long
      @mod_info["sv_only"] = byte_buffer.get_byte == 1
      @mod_info["cl_dll"] = byte_buffer.get_byte == 1
    end
    
    @secure = byte_buffer.get_byte == 1
    @number_of_bots = byte_buffer.get_byte
  end
  
end
