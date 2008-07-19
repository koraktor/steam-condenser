# The SteamPlayer class represents a player connected to a server
class SteamPlayer
  
  # Creates a new SteamPlayer object based on the given information
  def initialize(id, name, score, connect_time)
    @connect_time = connect_time
    @id = id
    @name = name
    @score = score
  end
end
