class A2A_PING_RequestPacket < SteamPacket
  def initialize
    super SteamPacket::A2A_PING_REQUEST_HEADER
  end
end