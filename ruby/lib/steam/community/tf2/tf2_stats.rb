# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'steam/community/game_stats'
require 'steam/community/tf2/tf2_beta_inventory'
require 'steam/community/tf2/tf2_class_factory'
require 'steam/community/tf2/tf2_inventory'

# This class represents the game statistics for a single user in Team Fortress
# 2
#
# @author Sebastian Staudt
class TF2Stats < GameStats

  # Returns the total points this player has achieved in his career
  #
  # @return [Fixnum] This player's accumulated points
  attr_reader :accumulated_points

  # Creates a `TF2Stats` instance by calling the super constructor with the
  # game name `'tf2'`
  #
  # @param [String, Fixnum] steam_id The custom URL or 64bit Steam ID of the
  #        user
  # @param [Boolean] beta If `true, creates stats for the public TF2 beta
  def initialize(steam_id, beta = false)
    super steam_id, (beta ? '520' : 'tf2')

    if public? && !@xml_data.elements['stats/accumulatedPoints'].nil?
      @accumulated_points = @xml_data.elements['stats/accumulatedPoints'].text.to_i
    end
  end

  # Returns the statistics for all Team Fortress 2 classes for this user
  #
  # If the classes haven't been parsed already, parsing is done now.
  #
  # @return [Hash<String, TF2Class>] A hash storing individual stats for each
  #         Team Fortress 2 class
  def class_stats
    return unless public?

    if @class_stats.nil?
      @class_stats = Hash.new
      @xml_data.elements.each('stats/classData') do |class_data|
        @class_stats[class_data.elements['className'].text] = TF2ClassFactory.tf2_class(class_data)
      end
    end

    @class_stats
  end

  # Returns the current Team Fortress 2 inventory (a.k.a. backpack) of this
  # player
  #
  # @return [TF2Inventory] This player's TF2 backpack
  def inventory
    if @inventory.nil?
      inventory_class = (game_friendly_name == 'tf2') ? TF2Inventory : TF2BetaInventory
      @inventory = inventory_class.new(steam_id64) if @inventory.nil?
    end

    @inventory
  end

end
