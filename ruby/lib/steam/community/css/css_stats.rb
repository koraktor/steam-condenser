# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2010, Sebastian Staudt

require 'steam/community/css/css_map'
require 'steam/community/css/css_weapon'

# The CSSStats class represents the game statistics for a single user in
# Counter-Strike: Source
class CSSStats < GameStats

  MAPS = [ 'cs_assault', 'cs_compound', 'cs_havana', 'cs_italy', 'cs_militia',
           'cs_office', 'de_aztec', 'de_cbble', 'de_chateau', 'de_dust',
           'de_dust2', 'de_inferno', 'de_nuke', 'de_piranesi', 'de_port',
           'de_prodigy', 'de_tides', 'de_train' ]

  WEAPONS = [ 'deagle', 'usp', 'glock', 'p228', 'elite', 'fiveseven', 'awp',
              'ak47', 'm4a1', 'aug', 'sg552', 'sg550', 'galil', 'famas',
              'scout', 'g3sg1', 'p90', 'mp5navy', 'tmp', 'mac10', 'ump45',
              'm3', 'xm1014', 'm249', 'knife', 'grenade' ]

  attr_reader :last_match_costkill, :last_match_ct_wins, :last_match_damage,
              :last_match_deaths, :last_match_dominations,
              :last_match_favorite_weapon, :last_match_kdratio,
              :last_match_kills, :last_match_max_players, :last_match_money,
              :last_match_revenges, :last_match_stars, :last_match_t_wins,
              :last_match_wins, :total_accuracy, :total_blind_kills,
              :total_bombs_defused, :total_bombs_planted, :total_damage,
              :total_deaths, :total_domination_overkills, :total_dominations,
              :total_earned_money, :total_enemy_weapon_kills, :total_headshots,
              :total_hits, :total_hostages_rescued, :total_kdratio,
              :total_kills, :total_knife_kills, :total_logos_sprayed,
              :total_logos_sprayed, :total_pistol_rounds_won, :total_revenges,
              :total_rounds_lost, :total_rounds_played, :total_rounds_won,
              :total_seconds_played, :total_shots, :total_stars,
              :total_weapons_donated, :total_windows_broken,
              :total_zoomed_sniper_kills

  # Creates a CSSStats object by calling the super constructor with the game
  # name "cs:s"
  def initialize(steam_id)
    super steam_id, 'cs:s'

    if public?
      @last_match_costkill        = @xml_data.elements['stats/lastmatch/costkill'].text.to_i
      @last_match_ct_wins         = @xml_data.elements['stats/lastmatch/ct_wins'].text.to_i
      @last_match_damage          = @xml_data.elements['stats/lastmatch/dmg'].text.to_i
      @last_match_deaths          = @xml_data.elements['stats/lastmatch/deaths'].text.to_i
      @last_match_dominations     = @xml_data.elements['stats/lastmatch/dominations'].text.to_i
      @last_match_favorite_weapon = @xml_data.elements['stats/lastmatch/favwpn'].text
      @last_match_kills           = @xml_data.elements['stats/lastmatch/kills'].text.to_i
      @last_match_max_players     = @xml_data.elements['stats/lastmatch/max_players'].text.to_i
      @last_match_money           = @xml_data.elements['stats/lastmatch/money'].text.to_i
      @last_match_revenges        = @xml_data.elements['stats/lastmatch/revenges'].text.to_i
      @last_match_stars           = @xml_data.elements['stats/lastmatch/stars'].text.to_i
      @last_match_t_wins          = @xml_data.elements['stats/lastmatch/t_wins'].text.to_i
      @last_match_wins            = @xml_data.elements['stats/lastmatch/wins'].text.to_i
      @total_blind_kills          = @xml_data.elements['stats/lifetime/blindkills'].text.to_i
      @total_bombs_defused        = @xml_data.elements['stats/lifetime/bombsdefused'].text.to_i
      @total_bombs_planted        = @xml_data.elements['stats/lifetime/bombsplanted'].text.to_i
      @total_damage               = @xml_data.elements['stats/lifetime/dmg'].text.to_i
      @total_deaths               = @xml_data.elements['stats/summary/deaths'].text.to_i
      @total_domination_overkills = @xml_data.elements['stats/lifetime/dominationoverkills'].text.to_i
      @total_dominations          = @xml_data.elements['stats/lifetime/dominations'].text.to_i
      @total_earned_money         = @xml_data.elements['stats/lifetime/money'].text.to_i
      @total_enemy_weapon_kills   = @xml_data.elements['stats/lifetime/enemywpnkills'].text.to_i
      @total_headshots            = @xml_data.elements['stats/lifetime/headshots'].text.to_i
      @total_hits                 = @xml_data.elements['stats/summary/shotshit'].text.to_i
      @total_hostages_rescued     = @xml_data.elements['stats/lifetime/hostagesrescued'].text.to_i
      @total_kills                = @xml_data.elements['stats/summary/kills'].text.to_i
      @total_knife_kills          = @xml_data.elements['stats/lifetime/knifekills'].text.to_i
      @total_logos_sprayed        = @xml_data.elements['stats/lifetime/decals'].text.to_i
      @total_nightvision_damage   = @xml_data.elements['stats/lifetime/nvgdmg'].text.to_i
      @total_pistol_rounds_won    = @xml_data.elements['stats/lifetime/pistolrounds'].text.to_i
      @total_revenges             = @xml_data.elements['stats/lifetime/revenges'].text.to_i
      @total_rounds_played        = @xml_data.elements['stats/summary/rounds'].text.to_i
      @total_rounds_won           = @xml_data.elements['stats/summary/wins'].text.to_i
      @total_seconds_played       = @xml_data.elements['stats/summary/timeplayed'].text.to_i
      @total_shots                = @xml_data.elements['stats/summary/shots'].text.to_i
      @total_stars                = @xml_data.elements['stats/summary/stars'].text.to_i
      @total_weapons_donated      = @xml_data.elements['stats/lifetime/wpndonated'].text.to_i
      @total_windows_broken       = @xml_data.elements['stats/lifetime/winbroken'].text.to_i
      @total_zoomed_sniper_kills  = @xml_data.elements['stats/lifetime/zsniperkills'].text.to_i

      @last_match_kdratio = @last_match_kills.to_f / @last_match_deaths
      @total_accuracy     = @total_hits.to_f / @total_shots
      @total_kdratio      = @total_kills.to_f / @total_deaths
      @total_rounds_lost  = @total_rounds_played - @total_rounds_won
    end
  end

  def map_stats
    return unless public?

    if @map_stats.nil?
      @map_stats = []
      maps_data = @xml_data.elements['stats/maps']

      MAPS.each do |map_name|
        @map_stats << CSSMap.new(maps_data, map_name)
      end
    end

    @map_stats
  end

  def weapon_stats
    return unless public?

    if @weapon_stats.nil?
      @weapon_stats = []
      weapons_data = @xml_data.elements['stats/weapons']

      WEAPONS.each do |weapon_name|
        @weapon_stats << CSSWeapon.new(weapons_data, weapon_name)
      end
    end

    @weapon_stats
  end

end
