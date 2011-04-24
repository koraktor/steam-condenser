# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'steam/community/cacheable'
require 'steam/community/game_item'
require 'steam/community/web_api'

# Provides basic functionality to represent an inventory of player in a game
module GameInventory

  attr_reader :items, :steam_id64

  @@attribute_schema = {}

  @@item_schema = {}

  @@qualities = {}

  @@schema_language = 'en'

  # Sets the language the schema should be fetched in (default is: +'en'+)
  def self.schema_language=(language)
    @@schema_language = language
  end

  # Creates a new inventory object for the given AppID and SteamID64. This
  # calls update to fetch the data and create the item instances contained in
  # this players backpack
  def initialize(steam_id64, fetch_now = true)
    @steam_id64 = steam_id64

    super fetch_now
  end

  # Returns the item at the given position in the inventory. The positions
  # range from 1 to 100 instead of the usual array indices (0 to 99).
  def [](index)
    @items[index - 1]
  end

  # Returns the AppID of the game this inventory class belongs to
  def app_id
    self.class.send :class_variable_get, :@@app_id
  end

  # Returns the attribute schema
  #
  # The schemas are fetched first if not done already
  def attribute_schema
    update_schema unless @@attribute_schema.key? app_id

    @@attribute_schema[app_id]
  end

  # Updates the contents of the inventory using Steam Web API
  def fetch
    result = WebApi.json!("IEconItems_#{app_id}", 'GetPlayerItems', 1, { :SteamID => @steam_id64 })
    item_class = self.class.send :class_variable_get, :@@item_class

    @items = []
    result[:items].each do |item_data|
      unless item_data.nil?
        item = item_class.new(self, item_data)
        @items[item.backpack_position - 1] = item
      end
    end
  end

  # Returns the item schema
  #
  # The schemas are fetched first if not done already
  def item_schema
    update_schema unless @@item_schema.key? app_id

    @@item_schema[app_id]
  end

  # Returns the quality schema
  #
  # The schemas are fetched first if not done already
  def qualities
    update_schema unless @@qualities.key? app_id

    @@qualities[app_id]
  end

  # Returns the number of items in the user's inventory
  def size
    @items.size
  end

  protected

  # Updates the item schema (this includes attributes and qualities) using the
  # +GetSchema+ method of interface +IEconItems_{AppID}+
  def update_schema
    params = {}
    params[:language] = @@schema_language unless @@schema_language.nil?
    result = WebApi.json!("IEconItems_#{app_id}", 'GetSchema', 1, params)

    @@attribute_schema[app_id] = {}
    result[:attributes].each do |attribute_data|
      @@attribute_schema[app_id][attribute_data[:name]] = attribute_data
    end

    @@item_schema[app_id] = []
    result[:items].each do |item_data|
      @@item_schema[app_id][item_data[:defindex]] = item_data
    end

    @@qualities[app_id] = []
    result[:qualities].each do |quality, id|
      @@qualities[app_id][id] = quality
    end
  end

end
