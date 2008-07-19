autoload "SteamPacket", "steam/packets/steam_packet"

# Creates a new A2A_RULES request object including the challenge_number
class A2A_RULES_RequestPacket < SteamPacket
  
  # Creates a new A2A_RULES request object including the challenge_number
  def initialize(challenge_number = "FFFFFF".to_i(16))
    super SteamPacket::A2A_RULES_REQUEST_HEADER, challenge_number
  end

  # Returns a packed version of the A2A_RULES request
  def to_s
    return [0xFF, 0xFF, 0xFF, 0xFF, @header_data, @content_data].pack("c5l")
  end  
end
