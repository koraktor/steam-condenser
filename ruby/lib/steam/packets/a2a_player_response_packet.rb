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
    
    number_of_players = @content_data[0]
    players_data = @content_data[1..-1]

    @player_array = Array.new(number_of_players)
    
    while players_data.size > 0
      player_data = players_data.unpack("cZ*Vea*")
      @player_array = SteamPlayer.new player_data[0..3]
      players_data = player_data[4]
    end
  end
  
  # Returns the array containing SteamPlayer objects for all players on the
  # server
  def get_player_array
    return @player_array
  end
end
