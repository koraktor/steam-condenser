# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009-2010, Sebastian Staudt

require 'steam/community/dods/dods_weapon'
require 'steam/community/game_stats'

class DoDSStats

  include GameStats

  # Creates a DoDSStats object by calling the super constructor with the game
  # name "DoD:S"
  def initialize(steam_id)
    super steam_id, 'DoD:S'
  end

  # Returns a Hash of DoDSClass for this user containing all DoD:S classes.
  # If the classes haven't been parsed already, parsing is done now.
  def class_stats
    return unless public?

    if @class_stats.nil?
      @class_stats = {}
      @xml_data.elements.each('stats/classes/class') do |class_data|
        @class_stats[class_data.attributes['key']] = DoDSClass.new class_data
      end
    end

    @class_stats
  end

  # Returns a Hash of DoDSWeapon for this user containing all DoD:S weapons.
  # If the weapons haven't been parsed already, parsing is done now.
  def weapon_stats
    return unless public?

    if @weapon_stats.nil?
      @weapon_stats = {}
      @xml_data.elements.each('stats/weapons/weapon') do |weapon_data|
        @weapon_stats[weapon_data.attributes['key']] = DoDSWeapon.new(weapon_data)
      end
    end

    @weapon_stats
  end

end
