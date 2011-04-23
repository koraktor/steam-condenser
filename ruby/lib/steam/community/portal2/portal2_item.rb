# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'steam/community/game_item'

# Represents a Portal 2 item
class Portal2Item

  include GameItem

  BOTS = [ :pbody, :atlas ]

  # Creates a new instance of a Portal2Item with the given data
  def initialize(inventory, item_data)
    super

    @equipped = {}
    BOTS.each_index do |class_id|
      @equipped[BOTS[class_id]] = (item_data[:inventory] & (1 << 16 + class_id) != 0)
    end
  end

  # Returns the symbols for each bot this player has equipped this item
  def bots_equipped?
    @equipped.reject { |bot_id, equipped| !equipped }
  end

  # Returns whether this item is equipped by this player at all
  def equipped?
    @equipped.has_value? true
  end

end
