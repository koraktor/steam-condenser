autoload "ByteBuffer", "byte_buffer"
autoload "SteamPacket", "steam/packets/steam_packet"

# The A2A_INFO_ResponsePacket class represents the response to a A2A_INFO
# request send to the server.
class A2A_INFO_ResponsePacket < SteamPacket
  
  # Creates a A2A_INFO response object based on the data received.
  def initialize(data)
    super SteamPacket::A2A_INFO_RESPONSE_HEADER, data

    byte_buffer = ByteBuffer.new data
    
    @protocol_version = byte_buffer.get_byte
    @server_name = byte_buffer.get_string
    @map_name = byte_buffer.get_string
    @game_directory = byte_buffer.get_string
    @game_description = byte_buffer.get_string
    @app_id = byte_buffer.get_short
    @number_of_players = byte_buffer.get_byte
    @max_players = byte_buffer.get_byte
    @number_of_bots = byte_buffer.get_byte
    @dedicated = byte_buffer.get_byte.chr
    @operating_system = byte_buffer.get_byte.chr
    @password_needed = byte_buffer.get_byte == 1
    @secure = byte_buffer.get_byte == 1
    @game_version = byte_buffer.get_string
    extra_data_flag = byte_buffer.get_byte
    
    if extra_data_flag & 0x80 > 0
      @server_port = byte_buffer.get_short
    end
    
    if extra_data_flag & 0x40 > 0
      @tv_port = byte_buffer.get_short
      @tv_name = byte_buffer.get_string
    end   
    
    if extra_data_flag & 0x20 > 0
      @server_tags = byte_buffer.get_string
    end
  end
  
  # Returns the hash containing information on the server
  def get_info_hash
    return Hash[
      *instance_variables.map { |var|
        if var != "@content_data" && var != "@header_data"
          [var[1..-1], instance_variable_get(var)]
        end
      }.compact.flatten
    ]
  end
end
