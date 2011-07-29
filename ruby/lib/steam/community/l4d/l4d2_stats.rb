# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2009-2011, Sebastian Staudt

require 'steam/community/l4d/abstract_l4d_stats'
require 'steam/community/l4d/l4d2_map'
require 'steam/community/l4d/l4d2_weapon'

# This class represents the game statistics for a single user in Left4Dead 2
#
# @author Sebastian Staudt
class L4D2Stats < GameStats

  include AbstractL4DStats

  # The names of the special infected in Left4Dead 2
  SPECIAL_INFECTED = SPECIAL_INFECTED + %w{charger jockey spitter}

  # Creates a `L4D2Stats` object by calling the super constructor with the game
  # name `'l4d2'`
  #
  # @param [String] steam_id The custom URL or 64bit Steam ID of the user
  def initialize(steam_id)
    super steam_id, 'l4d2'
  end

  # Returns a hash of lifetime statistics for this user like the time played
  #
  # If the lifetime statistics haven't been parsed already, parsing is done
  # now.
  #
  # There are only a few additional lifetime statistics for Left4Dead 2
  # which are not generated for Left4Dead, so this calls
  # {AbstractL4DStats#lifetime_stats} first and adds some additional stats.
  #
  # @return [Hash<String, Object>] The lifetime statistics of the player in
  #         Left4Dead 2
  def lifetime_stats
    return unless public?

    if @lifetime_stats.nil?
      super
      @lifetime_stats[:avg_adrenaline_shared]   = @xml_data.elements['stats/lifetime/adrenalineshared'].text.to_f
      @lifetime_stats[:avg_adrenaline_used]     = @xml_data.elements['stats/lifetime/adrenalineused'].text.to_f
      @lifetime_stats[:avg_defibrillators_used] = @xml_data.elements['stats/lifetime/defibrillatorsused'].text.to_f
    end

    @lifetime_stats
  end

  # Returns a hash of Scavenge statistics for this user like the number of
  # Scavenge rounds played
  #
  # If the Scavenge statistics haven't been parsed already, parsing is done
  # now.
  #
  # @return [Hash<String, Object>] The Scavenge statistics of the player
  def scavenge_stats
    return unless public?

    if @scavenge_stats.nil?
      @scavenge_stats = {}
      @scavenge_stats[:avg_cans_per_round] = @xml_data.elements['stats/scavenge/avgcansperround'].text.to_f
      @scavenge_stats[:perfect_rounds]     = @xml_data.elements['stats/scavenge/perfect16canrounds'].text.to_i
      @scavenge_stats[:rounds_lost]        = @xml_data.elements['stats/scavenge/roundslost'].text.to_i
      @scavenge_stats[:rounds_played]      = @xml_data.elements['stats/scavenge/roundsplayed'].text.to_i
      @scavenge_stats[:rounds_won]         = @xml_data.elements['stats/scavenge/roundswon'].text.to_i
      @scavenge_stats[:total_cans]         = @xml_data.elements['stats/scavenge/totalcans'].text.to_i

      @scavenge_stats[:maps] = {}
      @xml_data.elements.each('stats/scavenge/mapstats/map') do |map_data|
        map_id = map_data.elements['name'].text
        @scavenge_stats[:maps][map_id] = {}
        @scavenge_stats[:maps][map_id]['avg_round_score']     = map_data.elements['avgscoreperround'].text.to_i
        @scavenge_stats[:maps][map_id]['highest_game_score']  = map_data.elements['highgamescore'].text.to_i
        @scavenge_stats[:maps][map_id]['highest_round_score'] = map_data.elements['highroundscore'].text.to_i
        @scavenge_stats[:maps][map_id]['name']                = map_data.elements['fullname'].text
        @scavenge_stats[:maps][map_id]['rounds_played']       = map_data.elements['roundsplayed'].text.to_i
        @scavenge_stats[:maps][map_id]['rounds_won']          = map_data.elements['roundswon'].text.to_i
      end

      @scavenge_stats[:infected] = {}
      @xml_data.elements.each('stats/scavenge/infectedstats/special') do |infected_data|
        infected_id = infected_data.elements['name'].text
        @scavenge_stats[:infected][infected_id] = {}
        @scavenge_stats[:infected][infected_id]['max_damage_per_life']   = infected_data.elements['maxdmg1life'].text.to_i
        @scavenge_stats[:infected][infected_id]['max_pours_interrupted'] = infected_data.elements['maxpoursinterrupted'].text.to_i
        @scavenge_stats[:infected][infected_id]['special_attacks']       = infected_data.elements['specialattacks'].text.to_i
      end
    end

    @scavenge_stats
  end

  # Returns a hash of Survival statistics for this user like revived teammates
  #
  # If the Survival statistics haven't been parsed already, parsing is done
  # now.
  #
  # The XML layout for the Survival statistics for Left4Dead 2 differs a bit
  # from Left4Dead's Survival statistics. So we have to use a different way
  # of parsing for the maps and we use a different map class
  # (`L4D2Map`) which holds the additional information provided in
  # Left4Dead 2's statistics.
  #
  # @return [Hash<String, Object>] The Survival statistics of the player
  def survival_stats
    return unless public?

    if @survival_stats.nil?
      super
      @survival_stats[:maps] = {}
      @xml_data.elements.each('stats/survival/maps/map') do |map_data|
        map = L4D2Map.new(map_data)
        @survival_stats[:maps][map.id] = map
      end
    end

    @survival_stats
  end

  # Returns a hash of `L4D2Weapon` for this user containing all Left4Dead 2
  # weapons
  #
  # If the weapons haven't been parsed already, parsing is done now.
  #
  # @return [Hash<String, Object>] The weapon statistics for this player
  def weapon_stats
    return unless public?

    if @weapon_stats.nil?
      @weapon_stats = {}
      @xml_data.elements.each('stats/weapons/*') do |weapon_data|
        next unless weapon_data.has_elements?

        unless %w{bilejars molotov pipes}.include? weapon_data.name
          weapon = L4D2Weapon.new(weapon_data)
        else
          weapon = L4DExplosive.new(weapon_data)
        end

        @weapon_stats[weapon_data.name] = weapon
      end
    end

    @weapon_stats
  end

end
