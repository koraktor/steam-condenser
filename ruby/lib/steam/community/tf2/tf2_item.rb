# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2010, Sebastian Staudt

require 'json'

require 'steam/community/web_api'

# Represents a Team Fortress 2 item
class TF2Item

  include WebApi

  CLASSES = [ :scout, :sniper, :soldier, :demoman, :medic, :heavy,
                     :pyro, :spy ]

  attr_reader :attributes, :backpack_position, :class, :count, :defindex, :id,
              :level, :name, :quality, :slot, :type

  @@attribute_schema = nil

  @@item_schema = nil

  @@schema_language = 'en'

  # Returns the attribute schema
  #
  # The attribute schema is fetched first if not done already
  def self.attribute_schema
    update_schema if @@attribute_schema.nil?

    @@attribute_schema
  end

  # Returns the item schema
  #
  # The item schema is fetched first if not done already
  def self.item_schema
    update_schema if @@item_schema.nil?

    @@item_schema
  end

  # Sets the language the schema should be fetched in (default is: +'en'+)
  def self.schema_language=(language)
    @@schema_language = language
  end

  # Creates a new instance of a TF2Item with the given data
  def initialize(item_data)
    update_schema if @@item_schema.nil?

    @defindex          = item_data[:defindex]

    @backpack_position = item_data[:inventory] & 0xffff
    @class             = @@item_schema[@defindex][:item_class]
    @count             = item_data[:quantity]
    @id                = item_data[:id]
    @level             = item_data[:level]
    @name              = @@item_schema[@defindex][:item_name]
    @quality           = @@qualities[item_data[:quality]]
    @slot              = @@item_schema[@defindex][:item_slot]
    @type              = @@item_schema[@defindex][:item_type_name]

    @equipped = {}
    CLASSES.each_index do |class_id|
      @equipped[CLASSES[class_id]] = (item_data[:inventory] & (1 << 16 + class_id) != 0)
    end

    unless @@item_schema[@defindex][:attributes].nil?
      @attributes = @@item_schema[@defindex][:attributes][:attribute]
    end
  end

  # Returns the class symbols for each class this player has equipped this item
  def classes_equipped?
    @equipped.reject { |class_id, equipped| !equipped }
  end

  # Returns whether this item is equipped by this player at all
  def equipped?
    @equipped.has_value? true
  end

  protected

  # Updates the item schema (this includes attributes and qualities) using the
  # +GetSchema+ method of interface +ITFItems_440+
  def update_schema
    params = nil
    params = "language=#{@@schema_language}" unless @@schema_language.nil?
    result = JSON.parse(json('ITFItems_440', 'GetSchema', 1, params), { :symbolize_names => true })[:result]
    if result[:status] != 1
      raise SteamCondenserException.new("Failed to retrieve the item schema (status: #{result[:status]}).")
    end

    @@attribute_schema = {}
    result[:attributes][:attribute].each do |attribute_data|
      @@attribute_schema[attribute_data[:name]] = attribute_data
    end

    @@item_schema = []
    result[:items][:item].each do |item_data|
      @@item_schema[item_data[:defindex]] = item_data
    end

    @@qualities = []
    result[:qualities].each do |quality, id|
      @@qualities[id] = quality
    end
  end

end
