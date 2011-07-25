# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2009-2011, Sebastian Staudt

require 'steam/community/steam_id'
require 'steam/community/l4d/l4d_map'

# L4D2Map holds statistical information about maps played by a player in
# Survival mode of Left4Dead 2. The basic information provided is more or less
# the same for Left4Dead and Left4Dead 2, but parsing has to be done
# differently.
#
# @author Sebastian Staudt
class L4D2Map < L4DMap

  # Returns statistics about the items used by the player on this map
  #
  # @return [Hash<Symbol, Fixnum>] The items used by the player
  attr_reader :items

  # Returns the number of special infected killed by the player grouped by
  # the names of the special infected
  #
  # @return [Hash<Symbol, Fixnum>] The special infected killed by the player
  attr_reader :kills

  # Returns the SteamIDs of the teammates of the player in his best game on
  # this map
  #
  # @return [Array<SteamId>] The SteamIDs of the teammates in the best game
  attr_reader :teammates

  # The names of the special infected in Left4Dead 2
  INFECTED = %w{boomer charger common hunter jockey smoker spitter tank}

  # The items available in Left4Dead 2
  ITEMS    = %w{adrenaline defibs medkits pills}

  # Creates a new instance of a map based on the given XML data
  #
  # The map statistics for the Survival mode of Left4Dead 2 hold much more
  # information than those for Left4Dead, e.g. the teammates and items are
  # listed.
  #
  # @param [REXML::Element] map_data The XML data for this map
  def initialize(map_data)
    @id        = map_data.elements['img'].text.match(/http:\/\/steamcommunity\.com\/public\/images\/gamestats\/550\/(.*).jpg/)[1]
    @name      = map_data.elements['name'].text
    @played    = (map_data.elements['hasPlayed'].text.to_i == 1)

    if @played
      @best_time = map_data.elements['besttimemilliseconds'].text.to_f / 1000

      @teammates = []
      map_data.elements.each('teammates/steamID64') do |teammate|
        @teammates << SteamId.new(teammate.text, false)
      end

      @items = {}
      ITEMS.each do |item|
        @items[item] = map_data.elements["items_#{item}"].text.to_i
      end

      @kills = {}
      INFECTED.each do |infected|
        @kills[infected] = map_data.elements["kills_#{infected}"].text.to_i
      end

      case map_data.elements['medal'].text
        when 'gold'
          @medal = L4D2Map::GOLD
        when 'silver'
          @medal = L4D2Map::SILVER
        when 'bronze'
          @medal = L4D2Map::BRONZE
        else
          @medal = L4D2Map::NONE
      end
    end
  end

  # Returns whether the player has already played this map
  #
  # @return [Boolean] `true` if the player has already played this map
  def played?
    @played
  end

end
