# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2010-2011, Sebastian Staudt

require 'steam/community/game_item'

# Represents a Team Fortress 2 item#
#
# @author Sebastian Staudt
class TF2Item

  include GameItem

  CLASSES = [ :scout, :sniper, :soldier, :demoman, :medic, :heavy, :pyro, :spy ]

  # Creates a new instance of a TF2 item with the given data
  #
  # @param [TF2Inventory] inventory The inventory this item is contained in
  # @param [Hash<Symbol, Object>] item_data The data specifying this item
  # @throws WebApiException on Web API errors
  def initialize(inventory, item_data)
    super

    @equipped = {}
    CLASSES.each_index do |class_id|
      @equipped[CLASSES[class_id]] = (item_data[:inventory] & (1 << 16 + class_id) != 0)
    end
  end

  # Returns the class symbols for each class this player has equipped this item
  #
  # @return [Array<String>] The names of the classes this player has equipped
  #         this item
  def classes_equipped?
    @equipped.reject { |class_id, equipped| !equipped }
  end

  # Returns whether this item is equipped by this player at all
  #
  # @return `true` if the player has equipped this item at all
  def equipped?
    @equipped.has_value? true
  end

end
