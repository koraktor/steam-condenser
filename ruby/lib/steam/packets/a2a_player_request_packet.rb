class A2A_PLAYER_RequestPacket < SteamPacket
  def initialize(challenge_number = "FFFFFFFF".to_i(16))
    super SteamPacket::A2A_PLAYER_REQUEST_HEADER, challenge_number
  end
  
  def to_s
    return [0xFF, 0xFF, 0xFF, 0xFF, @header_data, @content_data].pack("c5l")
  end
end
