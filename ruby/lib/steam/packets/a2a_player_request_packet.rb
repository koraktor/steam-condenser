autoload "RequestWithChallenge", "steam/packets/request_with_challenge"
autoload "SteamPacket", "steam/packets/steam_packet"

# The A2A_PING_RequestPacket class represents a A2A_PING request send to the
# server.
class A2A_PLAYER_RequestPacket < SteamPacket
  
  include RequestWithChallenge
  
  # Creates a new A2A_PLAYER request object including the challenge_number
  def initialize(challenge_number = -1)
    super SteamPacket::A2A_PLAYER_REQUEST_HEADER, challenge_number
  end
  
end
