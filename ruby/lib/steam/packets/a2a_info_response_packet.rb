autoload "SteamPacket", "steam/packets/steam_packet"

# The A2A_INFO_ResponsePacket class represents the response to a A2A_INFO
# request send to the server.
class A2A_INFO_ResponsePacket < SteamPacket
  
  # Creates a A2A_INFO response object based on the data received.
  def initialize(data)
    super SteamPacket::A2A_INFO_RESPONSE_HEADER, data
    
    @protocol_version, @server_name, @map_name, @game_directory, @game_description, @app_id, @number_of_players, @max_players, @number_of_bots, @dedicated_byte, @os, @password_needed, @secure, @game_version, extended_data_flag, @game_port, @spectator_port, @spectator_server, @game_tags = data.unpack("cZ*Z*Z*Z*vcccccccZ*cvvZ*Z*")
    @dedicated_byte = @dedicated_byte.chr
    @os = @os.chr
  end
  
  # Returns the hash containing information on the server
  def get_info_hash
    return Hash[
      *instance_variables.map { |var|
        if var != "@content_data"
          [var[1..-1], instance_variable_get(var)]
        end
      }.compact.flatten
    ]
  end
end
