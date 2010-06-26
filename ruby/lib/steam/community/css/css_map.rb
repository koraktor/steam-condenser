# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2010, Sebastian Staudt

# CSSMap holds statistical information about maps played by a player in
# Counter-Strike: Source.
class CSSMap

  attr_reader :name, :rounds_lost, :rounds_played, :rounds_won,
              :rounds_won_percentage

  # Creates a new instance of CSSMap based on the assigned XML data
  def initialize(maps_data, map_name)
    @name          = map_name
    
    @favorite      = (maps_data.elements['favorite'].text == map_name)
    @rounds_played = maps_data.elements["#{@name}_rounds"].text.to_i
    @rounds_won    = maps_data.elements["#{@name}_wins"].text.to_i

    @rounds_lost = @rounds_played - @rounds_won
    @rounds_won_percentage = (@rounds_played > 0) ? @rounds_won.to_f / @rounds_played : 0
  end

  # Returns whether this map is the favorite map of this player
  def favorite?
    @favorite
  end

end
