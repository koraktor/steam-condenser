# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'steam/community/game_stats'
require 'steam/community/portal2/portal2_inventory'

# This class represents the game statistics for a single user in Portal 2
#
# @author Sebastian Staudt
class Portal2Stats < GameStats

  # Creates a `Portal2Stats` object by calling the super constructor with the
  # game name `'portal2'`
  #
  # @param [String, Fixnum] steam_id The custom URL or 64bit Steam ID of the
  #        user
  def initialize(steam_id)
    super steam_id, 'portal2'
  end

  # Returns the current Portal 2 inventory (a.k.a Robot Enrichment) of this
  # player
  #
  # @return [TF2Inventory] This player's Portal 2 inventory
  def inventory
    @inventory = Portal2Inventory.new(steam_id64) if @inventory.nil?
    @inventory
  end

end
