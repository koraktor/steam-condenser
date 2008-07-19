autoload "SteamPacket", "steam/packets/steam_packet"

# The A2A_PING_RequestPacket class represents a A2A_PING request send to the
# server.
class A2A_PING_RequestPacket < SteamPacket
  
  # Creates a new A2A_PING request object
  def initialize
    super SteamPacket::A2A_PING_REQUEST_HEADER
  end
end