class A2A_PING_RequestPacket < SteamPacket
  def initialize
    super SteamPacket::A2A_INFO_REQUEST_HEADER, "Source Engine Query"
  end
end