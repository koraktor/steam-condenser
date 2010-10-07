# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2010, Sebastian Staudt

require 'steam/community/css/css_map'
require 'steam/community/css/css_weapon'

# The CSSStats class represents the game statistics for a single user in
# Counter-Strike: Source
class CSSStats

  include GameStats

  MAPS = [ 'cs_assault', 'cs_compound', 'cs_havana', 'cs_italy', 'cs_militia',
           'cs_office', 'de_aztec', 'de_cbble', 'de_chateau', 'de_dust',
           'de_dust2', 'de_inferno', 'de_nuke', 'de_piranesi', 'de_port',
           'de_prodigy', 'de_tides', 'de_train' ]

  WEAPONS = [ 'deagle', 'usp', 'glock', 'p228', 'elite', 'fiveseven', 'awp',
              'ak47', 'm4a1', 'aug', 'sg552', 'sg550', 'galil', 'famas',
              'scout', 'g3sg1', 'p90', 'mp5navy', 'tmp', 'mac10', 'ump45',
              'm3', 'xm1014', 'm249', 'knife', 'grenade' ]

  attr_reader :last_match_stats, :total_stats

  # Creates a CSSStats object by calling the super constructor with the game
  # name "cs:s"
  def initialize(steam_id)
    super steam_id, 'cs:s'

    if public?
      @last_match_stats = {}
      @total_stats      = {}

      @last_match_stats[:cost_per_kill]   = @xml_data.elements['stats/lastmatch/costkill'].text.to_f
      @last_match_stats[:ct_wins]         = @xml_data.elements['stats/lastmatch/ct_wins'].text.to_i
      @last_match_stats[:damage]          = @xml_data.elements['stats/lastmatch/dmg'].text.to_i
      @last_match_stats[:deaths]          = @xml_data.elements['stats/lastmatch/deaths'].text.to_i
      @last_match_stats[:dominations]     = @xml_data.elements['stats/lastmatch/dominations'].text.to_i
      @last_match_stats[:favorite_weapon] = @xml_data.elements['stats/lastmatch/favwpn'].text
      @last_match_stats[:kills]           = @xml_data.elements['stats/lastmatch/kills'].text.to_i
      @last_match_stats[:max_players]     = @xml_data.elements['stats/lastmatch/max_players'].text.to_i
      @last_match_stats[:money]           = @xml_data.elements['stats/lastmatch/money'].text.to_i
      @last_match_stats[:revenges]        = @xml_data.elements['stats/lastmatch/revenges'].text.to_i
      @last_match_stats[:stars]           = @xml_data.elements['stats/lastmatch/stars'].text.to_i
      @last_match_stats[:t_wins]          = @xml_data.elements['stats/lastmatch/t_wins'].text.to_i
      @last_match_stats[:wins]            = @xml_data.elements['stats/lastmatch/wins'].text.to_i
      @total_stats[:blind_kills]          = @xml_data.elements['stats/lifetime/blindkills'].text.to_i
      @total_stats[:bombs_defused]        = @xml_data.elements['stats/lifetime/bombsdefused'].text.to_i
      @total_stats[:bombs_planted]        = @xml_data.elements['stats/lifetime/bombsplanted'].text.to_i
      @total_stats[:damage]               = @xml_data.elements['stats/lifetime/dmg'].text.to_i
      @total_stats[:deaths]               = @xml_data.elements['stats/summary/deaths'].text.to_i
      @total_stats[:domination_overkills] = @xml_data.elements['stats/lifetime/dominationoverkills'].text.to_i
      @total_stats[:dominations]          = @xml_data.elements['stats/lifetime/dominations'].text.to_i
      @total_stats[:earned_money]         = @xml_data.elements['stats/lifetime/money'].text.to_i
      @total_stats[:enemy_weapon_kills]   = @xml_data.elements['stats/lifetime/enemywpnkills'].text.to_i
      @total_stats[:headshots]            = @xml_data.elements['stats/lifetime/headshots'].text.to_i
      @total_stats[:hits]                 = @xml_data.elements['stats/summary/shotshit'].text.to_i
      @total_stats[:hostages_rescued]     = @xml_data.elements['stats/lifetime/hostagesrescued'].text.to_i
      @total_stats[:kills]                = @xml_data.elements['stats/summary/kills'].text.to_i
      @total_stats[:knife_kills]          = @xml_data.elements['stats/lifetime/knifekills'].text.to_i
      @total_stats[:logos_sprayed]        = @xml_data.elements['stats/lifetime/decals'].text.to_i
      @total_stats[:nightvision_damage]   = @xml_data.elements['stats/lifetime/nvgdmg'].text.to_i
      @total_stats[:pistol_rounds_won]    = @xml_data.elements['stats/lifetime/pistolrounds'].text.to_i
      @total_stats[:revenges]             = @xml_data.elements['stats/lifetime/revenges'].text.to_i
      @total_stats[:rounds_played]        = @xml_data.elements['stats/summary/rounds'].text.to_i
      @total_stats[:rounds_won]           = @xml_data.elements['stats/summary/wins'].text.to_i
      @total_stats[:seconds_played]       = @xml_data.elements['stats/summary/timeplayed'].text.to_i
      @total_stats[:shots]                = @xml_data.elements['stats/summary/shots'].text.to_i
      @total_stats[:stars]                = @xml_data.elements['stats/summary/stars'].text.to_i
      @total_stats[:weapons_donated]      = @xml_data.elements['stats/lifetime/wpndonated'].text.to_i
      @total_stats[:windows_broken]       = @xml_data.elements['stats/lifetime/winbroken'].text.to_i
      @total_stats[:zoomed_sniper_kills]  = @xml_data.elements['stats/lifetime/zsniperkills'].text.to_i

      @last_match_stats[:kdratio] = @last_match_stats[:kills].to_f / @last_match_stats[:deaths]
      @total_stats[:accuracy]     = @total_stats[:hits].to_f / @total_stats[:shots]
      @total_stats[:kdratio]      = @total_stats[:kills].to_f / @total_stats[:deaths]
      @total_stats[:rounds_lost]  = @total_stats[:rounds_played] - @total_stats[:rounds_won]
    end
  end

  # Returns a Hash of CSSMap for this user containing all Counter-Strike:
  # Source maps. If the maps haven't been parsed already, parsing is done now.
  def map_stats
    return unless public?

    if @map_stats.nil?
      @map_stats = {}
      maps_data = @xml_data.elements['stats/maps']

      MAPS.each do |map_name|
        @map_stats[map_name] = CSSMap.new(map_name, maps_data)
      end
    end

    @map_stats
  end

  # Returns a Hash of CSSWeapon for this user containing all Counter-Strike:
  # Source weapons. If the weapons haven't been parsed already, parsing is done
  # now.
  def weapon_stats
    return unless public?

    if @weapon_stats.nil?
      @weapon_stats = {}
      weapons_data = @xml_data.elements['stats/weapons']

      WEAPONS.each do |weapon_name|
        @weapon_stats[weapon_name] = CSSWeapon.new(weapon_name, weapons_data)
      end
    end

    @weapon_stats
  end

end
