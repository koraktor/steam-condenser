autoload "SteamPacket", "steam/packets/steam_packet"

# The A2A_PING_RequestPacket class represents a A2A_PING request send to the
# server.
class A2A_PLAYER_RequestPacket < SteamPacket
  
  # Creates a new A2A_PLAYER request object including the challenge_number
  def initialize(challenge_number = "FFFFFFFF".to_i(16))
    super SteamPacket::A2A_PLAYER_REQUEST_HEADER, challenge_number
  end
  
  # Returns a packed version of the A2A_PLAYER request
  def to_s
    return [0xFF, 0xFF, 0xFF, 0xFF, @header_data, @content_data].pack("c5l")
  end
end
