# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2009-2011, Sebastian Staudt

# This class holds statistical information about a map played by a player in
# Survival mode of Left4Dead
#
# @author Sebastian Staudt
class L4DMap

  # Returns the best survival time of this player on this map
  #
  # @return [Float] The best survival time of this player on this map
  attr_reader :best_time

  # Returns the ID of this map
  #
  # @return [String] The ID of this map
  attr_reader :id

  # Returns the highest medal this player has won on this map
  #
  # @return [Fixnum] The highest medal won by this player on this map
  attr_reader :medal

  # Returns the name of the map
  #
  # @return [String] The name of the map
  attr_reader :name

  # Returns the number of times this map has been played by this player
  #
  # @return [Fixnum] The number of times this map has been played
  attr_reader :times_played

  GOLD   = 1
  SILVER = 2
  BRONZE = 3
  NONE   = 0

  # Creates a new instance of a Left4Dead Survival map based on the given
  # XML data
  #
  # @param [REXML::Element] map_data The XML data for this map
  def initialize(map_data)
    @best_time    = map_data.elements['besttimeseconds'].text.to_f
    @id           = map_data.name
    @name         = map_data.elements['name'].text
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
