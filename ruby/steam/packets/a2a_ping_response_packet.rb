class A2A_PING_ResponsePacket < SteamPacket
	def initialize(data)
		if data != "00000000000000\0"
			raise Exception.new("Wrong formatted A2A_PING Response Packet.")
		end
		super SteamPacket::A2A_PING_RESPONSE_HEADER, data
	end
end