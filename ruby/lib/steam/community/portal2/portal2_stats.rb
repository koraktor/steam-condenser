# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2011, Sebastian Staudt

require 'steam/community/game_stats'
require 'steam/community/portal2/portal2_inventory'

# The Portal2Stats class represents the game statistics for a single user in
# Portal 2
class Portal2Stats < GameStats

  attr_reader :accumulated_points

  # Creates a Portal2Stats object by calling the super constructor with the
  # game name "portal2"
  def initialize(steam_id)
    super steam_id, 'portal2'
  end

  # Returns the current Portal 2 inventory (a.k.a Robot Enrichment) of this
  # player
  def inventory
    @inventory = Portal2Inventory.new(steam_id64) if @inventory.nil?
    @inventory
  end

end
