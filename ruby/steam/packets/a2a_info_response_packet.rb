class A2A_INFO_ResponsePacket < SteamPacket
	@mapName
	
	@serverName
  
  def self.read_from_socket(socket)
    server_info = Hash.new
    server_info["network_verson"] = socket.read_from_buffer 1
    
    return self.new server_info
  end
	
	def initialize(server_info)
   puts server_info.inspect
    
		unless server_info.is_a? Hash
			raise Exception.new("Argument 1 should be an array.")
		end
		super SteamPacket::A2A_INFO_RESPONSE_HEADER
    
    server_info.each_pair do |info_key, info_value|
      @info_key = info_value
    end
	end
	
	def get_map_name()
		return @mapName;
	end
	
	def get_server_name()
		return @serverName;
	end
end