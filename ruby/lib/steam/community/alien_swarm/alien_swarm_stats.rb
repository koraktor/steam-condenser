# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2010, Sebastian Staudt

require 'steam/community/alien_swarm/alien_swarm_mission'
require 'steam/community/alien_swarm/alien_swarm_weapon'

# The AlienSwarmStats class represents the game statistics for a single user in
# Alien Swarm
class AlienSwarmStats < GameStats

  attr_reader :lifetime_stats

  BASE_URL = 'http://steamcommunity.com/public/images/gamestats/swarm/'

  WEAPONS = [ 'Autogun', 'Cannon_Sentry', 'Chainsaw', 'Flamer',
              'Grenade_Launcher', 'Hand_Grenades', 'Hornet_Barrage',
              'Incendiary_Sentry', 'Laser_Mines', 'Marskman_Rifle', 'Minigun',
              'Mining_Laser', 'PDW', 'Pistol', 'Prototype_Rifle', 'Rail_Rifle',
              'Rifle', 'Rifle_Grenade', 'Sentry_Gun', 'Shotgun',
              'Tesla_Cannon', 'Vindicator', 'Vindicator_Grenade' ]

  # Creates an AlienSwarmStats object by calling the super constructor with the
  # game name "alienswarm"
  def initialize(steam_id)
    super steam_id, 'alienswarm'

    if public?
      @hours_played = @xml_data.elements['stats/lifetime/timeplayed'].text

      @lifetime_stats = {}
      @lifetime_stats[:accuracy]            = @xml_data.elements['stats/lifetime/accuracy'].text.to_f
      @lifetime_stats[:aliens_burned]       = @xml_data.elements['stats/lifetime/aliensburned'].text.to_i
      @lifetime_stats[:aliens_killed]       = @xml_data.elements['stats/lifetime/alienskilled'].text.to_i
      @lifetime_stats[:campaigns]           = @xml_data.elements['stats/lifetime/campaigns'].text.to_i
      @lifetime_stats[:damage_taken]        = @xml_data.elements['stats/lifetime/damagetaken'].text.to_i
      @lifetime_stats[:experience]          = @xml_data.elements['stats/lifetime/experience'].text.to_i
      @lifetime_stats[:experience_required] = @xml_data.elements['stats/lifetime/xprequired'].text.to_i
      @lifetime_stats[:fast_hacks]          = @xml_data.elements['stats/lifetime/fasthacks'].text.to_i
      @lifetime_stats[:friendly_fire]       = @xml_data.elements['stats/lifetime/friendlyfire'].text.to_i
      @lifetime_stats[:games_successful]    = @xml_data.elements['stats/lifetime/gamessuccess'].text.to_i
      @lifetime_stats[:healing]             = @xml_data.elements['stats/lifetime/healing'].text.to_i
      @lifetime_stats[:kills_per_hour]      = @xml_data.elements['stats/lifetime/killsperhour'].text.to_f
      @lifetime_stats[:level]               = @xml_data.elements['stats/lifetime/level'].text.to_i
      @lifetime_stats[:promotion]           = @xml_data.elements['stats/lifetime/promotion'].text.to_i
      @lifetime_stats[:promotion_img]       = BASE_URL + @xml_data.elements['stats/lifetime/promotionpic'].text if @lifetime_stats[:promotion] > 0
      @lifetime_stats[:next_unlock]         = @xml_data.elements['stats/lifetime/nextunlock'].text
      @lifetime_stats[:next_unlock_img]     = BASE_URL + @xml_data.elements['stats/lifetime/nextunlockimg'].text
      @lifetime_stats[:shots_fired]         = @xml_data.elements['stats/lifetime/shotsfired'].text.to_i
      @lifetime_stats[:total_games]         = @xml_data.elements['stats/lifetime/totalgames'].text.to_i

      @lifetime_stats[:games_successful_percentage] = (@lifetime_stats[:total_games] > 0) ? @lifetime_stats[:games_successful].to_f / @lifetime_stats[:total_games] : 0;
    end
  end

  # Returns a Hash of favorites for this user like weapons and marine.
  # If the favorites haven't been parsed already, parsing is done now.
  def favorites
    return unless public?

    if @favorites.nil?
      @favorites = {}
      @favorites[:class]                       = @xml_data.elements['stats/favorites/class'].text
      @favorites[:class_img]                   = @xml_data.elements['stats/favorites/classimg'].text
      @favorites[:class_percentage]            = @xml_data.elements['stats/favorites/classpct'].text.to_f
      @favorites[:difficulty]                  = @xml_data.elements['stats/favorites/difficulty'].text
      @favorites[:difficulty_percentage]       = @xml_data.elements['stats/favorites/difficultypct'].text.to_f
      @favorites[:extra]                       = @xml_data.elements['stats/favorites/extra'].text
      @favorites[:extra_img]                   = @xml_data.elements['stats/favorites/extraimg'].text
      @favorites[:extra_percentage]            = @xml_data.elements['stats/favorites/extrapct'].text.to_f
      @favorites[:marine]                      = @xml_data.elements['stats/favorites/marine'].text
      @favorites[:marine_img]                  = @xml_data.elements['stats/favorites/marineimg'].text
      @favorites[:marine_percentage]           = @xml_data.elements['stats/favorites/marinepct'].text.to_f
      @favorites[:mission]                     = @xml_data.elements['stats/favorites/mission'].text
      @favorites[:mission_img]                 = @xml_data.elements['stats/favorites/missionimg'].text
      @favorites[:mission_percentage]          = @xml_data.elements['stats/favorites/missionpct'].text.to_f
      @favorites[:primary_weapon]              = @xml_data.elements['stats/favorites/primary'].text
      @favorites[:primary_weapon_img]          = @xml_data.elements['stats/favorites/primaryimg'].text
      @favorites[:primary_weapon_percentage]   = @xml_data.elements['stats/favorites/primarypct'].text.to_f
      @favorites[:secondary_weapon]            = @xml_data.elements['stats/favorites/secondary'].text
      @favorites[:secondary_weapon_img]        = @xml_data.elements['stats/favorites/secondaryimg'].text
      @favorites[:secondary_weapon_percentage] = @xml_data.elements['stats/favorites/secondarypct'].text.to_f
    end

    @favorites
  end

  # Returns a Hash of item stats for this user like ammo deployed and medkits
  # used. If the items haven't been parsed already, parsing is done now.
  def item_stats
    return unless public?

    if @item_stats.nil?
      @item_stats = {}
      @item_stats[:ammo_deployed]             = @xml_data.elements['stats/weapons/ammo_deployed'].text.to_i
      @item_stats[:sentryguns_deployed]       = @xml_data.elements['stats/weapons/sentryguns_deployed'].text.to_i
      @item_stats[:sentry_flamers_deployed]   = @xml_data.elements['stats/weapons/sentry_flamers_deployed'].text.to_i
      @item_stats[:sentry_freeze_deployed]    = @xml_data.elements['stats/weapons/sentry_freeze_deployed'].text.to_i
      @item_stats[:sentry_cannon_deployed]    = @xml_data.elements['stats/weapons/sentry_cannon_deployed'].text.to_i
      @item_stats[:medkits_used]              = @xml_data.elements['stats/weapons/medkits_used'].text.to_i
      @item_stats[:flares_used]               = @xml_data.elements['stats/weapons/flares_used'].text.to_i
      @item_stats[:adrenaline_used]           = @xml_data.elements['stats/weapons/adrenaline_used'].text.to_i
      @item_stats[:tesla_traps_deployed]      = @xml_data.elements['stats/weapons/tesla_traps_deployed'].text.to_i
      @item_stats[:freeze_grenades_thrown]    = @xml_data.elements['stats/weapons/freeze_grenades_thrown'].text.to_i
      @item_stats[:electric_armor_used]       = @xml_data.elements['stats/weapons/electric_armor_used'].text.to_i
      @item_stats[:healgun_heals]             = @xml_data.elements['stats/weapons/healgun_heals'].text.to_i
      @item_stats[:healgun_heals_self]        = @xml_data.elements['stats/weapons/healgun_heals_self'].text.to_i
      @item_stats[:healbeacon_heals]          = @xml_data.elements['stats/weapons/healbeacon_heals'].text.to_i
      @item_stats[:healbeacon_heals_self]     = @xml_data.elements['stats/weapons/healbeacon_heals_self'].text.to_i
      @item_stats[:damage_amps_used]          = @xml_data.elements['stats/weapons/damage_amps_used'].text.to_i
      @item_stats[:healbeacons_deployed]      = @xml_data.elements['stats/weapons/healbeacons_deployed'].text.to_i
      @item_stats[:healbeacon_heals_pct]      = @xml_data.elements['stats/weapons/healbeacon_heals_pct'].text.to_f
      @item_stats[:healgun_heals_pct]         = @xml_data.elements['stats/weapons/healgun_heals_pct'].text.to_f
      @item_stats[:healbeacon_heals_pct_self] = @xml_data.elements['stats/weapons/healbeacon_heals_pct_self'].text.to_f
      @item_stats[:healgun_heals_pct_self]    = @xml_data.elements['stats/weapons/healgun_heals_pct_self'].text.to_f
    end

    @item_stats
  end

  # Returns a Hash of AlienSwarmMission for this user containing all Alien
  # Swarm missions. If the missions haven't been parsed already, parsing is
  # done now.
  def mission_stats
    return unless public?

    if @mission_stats.nil?
      @mission_stats = {}
      @xml_data.elements.each('stats/missions/*') do |mission_data|
        @mission_stats[mission_data.name] = AlienSwarmMission.new(mission_data)
      end
    end

    @mission_stats
  end

  # Returns a Hash of AlienSwarmWeapon for this user containing all Alien Swarm
  # weapons. If the weapons haven't been parsed already, parsing is done now.
  def weapon_stats
    return unless public?

    if @weapon_stats.nil?
      @weapon_stats = {}
      WEAPONS.each do |weapon_node|
        weapon_data = @xml_data.elements["stats/weapons/#{weapon_node}"]
        weapon = AlienSwarmWeapon.new(weapon_data)
        @weapon_stats[weapon.name] = weapon
      end
    end

    @weapon_stats
  end

end
