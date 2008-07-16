class A2A_PLAYER_ResponsePacket < SteamPacket
  def initialize(content_data)
    if content_data == nil
      raise Exception.new("Wrong formatted A2A_PLAYER response packet.")
    end
    
    super SteamPacket::A2A_PLAYER_RESPONSE_HEADER, content_data
    
    number_of_players = @content_data[0]
    players_data = @content_data[1, @content_data.size]

    @player_array = Array.new
    
    while players_data.size > 0
      player_data = players_data.unpack("cZ*Vea*")
      players_data = player_data[4]
      @player_array.push player_data[0..3]
    end
  end
  
  def get_player_array
    return @player_array
  end
end
