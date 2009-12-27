# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009, Sebastian Staudt

require 'steam/community/l4d/l4d_map'

# L4D2Map holds statistical information about maps played by a player in
# Survival mode of Left4Dead 2. The basic information provided is more or less
# the same for Left4Dead and Left4Dead 2, but parsing has to be done
# differently.
class L4D2Map < L4DMap

  attr_reader :items, :kills, :teammates

  ITEMS    = %w{adrenaline defibs medkits pills}
  INFECTED = %w{boomer charger common hunter jockey smoker spitter tank}

  # Creates a new instance of L4D2Map based on the assigned XML data
  #
  # The map statistics for the Survival mode of Left4Dead 2 hold much more
  # information than those for Left4Dead, e.g. the teammates and items are
  # listed.
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

  def played?
    @played
  end

end
