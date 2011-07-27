# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'steam/community/cacheable'
require 'steam/community/game_item'
require 'steam/community/web_api'

# Provides basic functionality to represent an inventory of player in a game
#
# @author Sebastian Staudt
module GameInventory

  # Returns an array of all items in this players inventory.
  #
  # @return [Array<GameItem>] All items in the backpack
  attr_reader :items

  # Returns the 64bit SteamID of the player owning this inventory
  #
  # @return [String] The 64bit SteamID
  attr_reader :steam_id64

  @@attribute_schema = {}

  @@item_schema = {}

  @@qualities = {}

  @@schema_language = 'en'

  # Sets the language the schema should be fetched in (default is: `'en'`)
  #
  # @param [String] The ISO 639-1 code of the schema language
  def self.schema_language=(language)
    @@schema_language = language
  end

  # Creates a new inventory object for the given AppID and SteamID64. This
  # calls update to fetch the data and create the item instances contained in
  # this players backpack
  #
  # @param [Fixnum] steam_id64 The 64bit SteamID of the player to get the
  #        inventory for
  # @param [Boolean] fetch_now if `true` the data will be fetched immediately
  def initialize(steam_id64, fetch_now = true)
    @steam_id64 = steam_id64

    super fetch_now
  end

  # Returns the item at the given position in the inventory. The positions
  # range from 1 to 100 instead of the usual array indices (0 to 99).
  #
  # @return [GameItem] The item at the given position in the inventory
  def [](index)
    @items[index - 1]
  end

  # Returns the application ID of the game this inventory class belongs to
  #
  # @return [Fixnum] The application ID of the game
  def app_id
    self.class.send :class_variable_get, :@@app_id
  end

  # Returns the attribute schema
  #
  # The schemas are fetched first if not done already
  #
  # @return [Hash<String, Hash<String, Object>>] The attribute schema for the
  #         game this inventory belongs to
  # @see #update_schema
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
  #
  # @return [Hash<Fixnum, Hash<String, Object>>] The item schema for the game
  #         this inventory belongs to
  # @see #upde_schema
  def item_schema
    update_schema unless @@item_schema.key? app_id

    @@item_schema[app_id]
  end

  # Returns the quality schema
  #
  # The schemas are fetched first if not done already
  #
  # @return [Hash<Fixnum, String>] The quality schema for this game
  # @see #update_schema
  def qualities
    update_schema unless @@qualities.key? app_id

    @@qualities[app_id]
  end

  # Returns the number of items in the user's inventory
  #
  # @return [Fixnum] The number of items in the inventory
  def size
    @items.size
  end

  protected

  # Updates the item schema (this includes attributes and qualities) using the
  # `GetSchema` method of interface `IEconItems_[AppID]`
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
