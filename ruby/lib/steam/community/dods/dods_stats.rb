# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2009-2011, Sebastian Staudt

require 'steam/community/dods/dods_weapon'
require 'steam/community/game_stats'

# The is class represents the game statistics for a single user in Day of
# Defeat: Source
#
# @author Sebastian Staudt
class DoDSStats < GameStats

  # Creates a `DoDSStats` instance by calling the super constructor with the
  # game name `'DoD:S'`
  #
  # @param [String, Fixnum] steam_id The custom URL or 64bit Steam ID of the
  #        user
  def initialize(steam_id)
    super steam_id, 'DoD:S'
  end

  # Returns a hash of `DoDSClass` for this user containing all DoD:S classes.
  #
  # If the classes haven't been parsed already, parsing is done now.
  #
  # @return [Hash<String, DoDSClass>] The class statistics for this user
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

  # Returns a Hash of `DoDSWeapon` for this user containing all DoD:S weapons.
  #
  # If the weapons haven't been parsed already, parsing is done now.
  #
  # @return [Hash<String, DoDSWeapon>] The weapon statistics for this user
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
