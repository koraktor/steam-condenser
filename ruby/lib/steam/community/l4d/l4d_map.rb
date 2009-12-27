# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009, Sebastian Staudt

# L4DMap holds statistical information about maps played by a player in
# Survival mode of Left4Dead.
class L4DMap

  attr_reader :best_time, :id, :medal, :name, :times_played

  GOLD   = 1
  SILVER = 2
  BRONZE = 3
  NONE   = 0

  # Creates a new instance of L4DMap based on the assigned XML data
  def initialize(map_data)
    @best_time = map_data.elements['besttimeseconds'].text.to_f
    @id = map_data.name
    @name = map_data.elements['name'].text
    @times_played = map_data.elements['timesplayed'].text.to_i

    case map_data.elements['medal'].text
      when 'gold'
        @medal = L4DMap::GOLD
      when 'silver'
        @medal = L4DMap::SILVER
      when 'bronze'
        @medal = L4DMap::BRONZE
      else
        @medal = L4DMap::NONE
    end
  end

end
