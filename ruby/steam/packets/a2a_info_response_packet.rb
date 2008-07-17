class A2A_INFO_ResponsePacket < SteamPacket
	def initialize(data)
    super SteamPacket::A2A_INFO_RESPONSE_HEADER, data
    
    @protocol_version, @server_name, @map_name, @game_directory, @game_description, @app_id, @number_of_players, @max_players, @number_of_bots, @dedicated_byte, @os, @password_needed, @secure, @game_version, extended_data_flag, @game_port, @spectator_port, @spectator_server, @game_tags = data.unpack("cZ*Z*Z*Z*vcccccccZ*cvvZ*Z*")
    @dedicated_byte = @dedicated_byte.chr
    @os = @os.chr
  end
	
	def get_map_name
		return @map_name
	end
	
	def get_server_name
		return @server_name
	end
end