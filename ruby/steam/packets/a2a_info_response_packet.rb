class A2A_INFO_ResponsePacket < SteamPacket
  @map_name = nil
	@server_name = nil
  
	def initialize(data)
    super SteamPacket::A2A_INFO_RESPONSE_HEADER, data
  end
	
	def get_map_name
		return @map_name
	end
	
	def get_server_name
		return @server_name
	end
end