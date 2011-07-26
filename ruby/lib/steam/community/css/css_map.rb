# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2010-2011, Sebastian Staudt

# Represents the stats for a Counter-Strike: Source map for a specific user
#
# @author Sebastian Staudt
class CSSMap

  # Returns the name of this map
  #
  # @return [String] The name of this map
  attr_reader :name

  # Returns the number of rounds the player has lost on this map
  #
  # @return [Fixnum] The number of rounds lost
  attr_reader :rounds_lost

  # Returns the number of rounds the player has played on this map
  #
  # @return [Fixnum] The number of rounds played
  attr_reader :rounds_played

  # Returns the number of rounds the player has won on this map
  #
  # @return [Fixnum] The number of rounds won
  attr_reader :rounds_won

  # Creates a new instance of a Counter-Strike: Source class based on the
  # given XML data
  #
  # @param [String] map_name The name of the map
  # @param [REXML::Element] maps_data The XML data of all maps
  def initialize(map_name, maps_data)
    @name          = map_name

    @favorite      = (maps_data.elements['favorite'].text == @name)
    @rounds_played = maps_data.elements["#{@name}_rounds"].text.to_i
    @rounds_won    = maps_data.elements["#{@name}_wins"].text.to_i

    @rounds_lost = @rounds_played - @rounds_won
    @rounds_won_percentage = (@rounds_played > 0) ? @rounds_won.to_f / @rounds_played : 0
  end

  # Returns whether this map is the favorite map of this player
  #
  # @return [Boolean] `true` if this is the favorite map
  def favorite?
    @favorite
  end

  # Returns the percentage of rounds the player has won on this map
  #
  # @return [Float] The percentage of rounds won
  def rounds_won_percentage
    (@rounds_played > 0) ? @rounds_won.to_f / @rounds_played : 0
  end

end
