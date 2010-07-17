# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2010, Sebastian Staudt

require 'steam/community/cacheable'
require 'steam/community/tf2/tf2_item'
require 'steam/community/web_api'

# Represents the inventory (aka. Backpack) of a Team Fortress 2 player
class TF2Inventory

  include Cacheable
  cacheable_with_ids :steam_id64

  attr_reader :items, :steam_id64

  # Creates a new inventory object for the given SteamID64. This calls update
  # to fetch the data and create the TF2Item instances contained in this
  # players backpack
  def initialize(steam_id64, fetch_now = true)
    @steam_id64 = steam_id64

    super(fetch_now)
  end

  # Returns the item at the given position in the backpack. The positions range
  # from 1 to 100 instead of the usual array indices (0 to 99).
  def [](index)
    @items[index - 1]
  end

  # Updates the contents of the backpack using Steam Web API
  def fetch
    result = WebApi.json!('ITFItems_440', 'GetPlayerItems', 1, { :SteamID => @steam_id64 })

    @items = []
    result[:items][:item].each do |item_data|
      unless item_data.nil?
        item = TF2Item.new(item_data)
        @items[item.backpack_position - 1] = item
      end
    end
  end

  # Returns the number of items in the user's backpack
  def size
    @items.size
  end

end
