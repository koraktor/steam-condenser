autoload "RequestWithChallenge", "steam/packets/request_with_challenge"
autoload "SteamPacket", "steam/packets/steam_packet"

# Creates a new A2A_RULES request object including the challenge_number
class A2A_RULES_RequestPacket < SteamPacket
  
  include RequestWithChallenge
  
  # Creates a new A2A_RULES request object including the challenge_number
  def initialize(challenge_number = -1)
    super SteamPacket::A2A_RULES_REQUEST_HEADER, challenge_number
  end

end
