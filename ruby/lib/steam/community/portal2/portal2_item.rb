# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'steam/community/game_item'

# Represents a Portal 2 item
#
# @author Sebastian Staudt
class Portal2Item

  include GameItem

  # The names of the bots available in Portal 2
  BOTS = [ :pbody, :atlas ]

  # Creates a new instance of a Portal 2 item with the given data
  #
  # @param [Portal2Inventory] inventory The inventory this item is contained in
  # @param [Hash<Symbol, Object>] item_data The data specifying this item
  # @raise [WebApiException] on Web API errors
  def initialize(inventory, item_data)
    super

    @equipped = {}
    BOTS.each_index do |class_id|
      @equipped[BOTS[class_id]] = (item_data[:inventory] & (1 << 16 + class_id) != 0)
    end
  end

  # Returns the symbols for each bot this player has equipped this item
  #
  # @return [Array<String>] The names of the bots this player has equipped this
  #         item
  def bots_equipped?
    @equipped.reject { |bot_id, equipped| !equipped }
  end

  # Returns whether this item is equipped by this player at all
  #
  # @return [Boolean] `true` if the player has equipped this item at all
  def equipped?
    @equipped.has_value? true
  end

end
