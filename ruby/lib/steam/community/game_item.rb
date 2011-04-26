# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'steam/community/web_api'

# Provides basic functionality to represent an item in a game
module GameItem

  attr_reader :attributes, :backpack_position, :class, :count, :defindex, :id,
              :level, :name, :quality, :slot, :type

  # Creates a new instance of a GameItem with the given data
  def initialize(inventory, item_data)
    @defindex          = item_data[:defindex]
    @backpack_position = item_data[:inventory] & 0xffff
    @class             = inventory.item_schema[@defindex][:item_class]
    @count             = item_data[:quantity]
    @id                = item_data[:id]
    @level             = item_data[:level]
    @name              = inventory.item_schema[@defindex][:item_name]
    @quality           = inventory.qualities[item_data[:quality]]
    @slot              = inventory.item_schema[@defindex][:item_slot]
    @tradeable         = !(item_data[:flag_cannot_trade] == true)
    @type              = inventory.item_schema[@defindex][:item_type_name]

    unless inventory.item_schema[@defindex][:attributes].nil?
      @attributes = inventory.item_schema[@defindex][:attributes]
    end
  end

  # Returns whether this item is tradeable
  def tradeable?
    @tradeable
  end

end
