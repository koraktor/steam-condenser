autoload "SteamPacket", "steam/packets/steam_packet"

# The A2A_PLAYER_ResponsePacket class represents the response to a A2A_PLAYER
# request send to the server.
class A2A_PLAYER_ResponsePacket < SteamPacket
  
  # Creates a A2A_PLAYER response object based on the data received.
  def initialize(content_data)
    if content_data == nil
      raise Exception.new("Wrong formatted A2A_PLAYER response packet.")
    end
    
    super SteamPacket::A2A_PLAYER_RESPONSE_HEADER, content_data
    
    @player_array = Array.new @content_data.get_byte
    
    while @content_data.remaining > 0
      player_data = @content_data.get_byte, @content_data.get_string, @content_data.get_long, @content_data.get_float
      @player_array[player_data[0]] = SteamPlayer.new *player_data[0..3]
    end
  end
  
  # Returns the array containing SteamPlayer objects for all players on the
  # server
  def get_player_array
    return @player_array
  end
end
