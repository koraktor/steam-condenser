# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009-2010, Sebastian Staudt

require 'steam/community/l4d/abstract_l4d_stats'
require 'steam/community/l4d/l4d_explosive'
require 'steam/community/l4d/l4d_map'
require 'steam/community/l4d/l4d_weapon'

class L4DStats < GameStats

  include AbstractL4DStats

  # Creates a L4DStats object by calling the super constructor with the game
  # name "l4d".
  def initialize(steam_id)
    super steam_id, 'l4d'
  end

  # Returns a Hash of Survival statistics for this user like revived teammates.
  # If the Survival statistics haven't been parsed already, parsing is done now.
  def survival_stats
    return unless public?

    if @survival_stats.nil?
      super
      @survival_stats['maps'] = {}
      @xml_data.elements.each('stats/survival/maps/*') do |map_data|
        @survival_stats['maps'][map_data.name] = L4DMap.new(map_data)
      end
    end

    @survival_stats
  end

  # Returns a Hash of L4DWeapon for this user containing all Left4Dead weapons.
  # If the weapons haven't been parsed already, parsing is done now.
  def weapon_stats
    return unless public?

    if @weapon_stats.nil?
      @weapon_stats = {}
      @xml_data.elements.each('stats/weapons/*') do |weapon_data|
        unless %w{molotov pipes}.include? weapon_data.name
          weapon = L4DWeapon.new(weapon_data)
        else
          weapon = L4DExplosive.new(weapon_data)
        end

        @weapon_stats[weapon_data.name] = weapon
      end
    end

    @weapon_stats
  end

end
