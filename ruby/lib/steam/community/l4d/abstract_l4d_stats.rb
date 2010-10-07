# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2009-2010, Sebastian Staudt

require 'steam/community/game_stats'

# AbstractL4DStats is an abstract base class for statistics for Left4Dead and
# Left4Dead 2. As both games have more or less the same statistics available in
# the Steam Community the code for both is pretty much the same.
module AbstractL4DStats

  include GameStats

  SPECIAL_INFECTED = %w(boomer hunter smoker tank)

  def initialize(steam_id, game_name)
    super steam_id, game_name

    if public?
      @most_recent_game = {}
      if @xml_data.elements['stats/mostrecentgame'].has_elements?
        @most_recent_game['difficulty']  = @xml_data.elements['stats/mostrecentgame/difficulty'].text
        @most_recent_game['escaped']     = (@xml_data.elements['stats/mostrecentgame/bEscaped'].text == 1)
        @most_recent_game['movie']       = @xml_data.elements['stats/mostrecentgame/movie'].text
        @most_recent_game['time_played'] = @xml_data.elements['stats/mostrecentgame/time'].text
      end
    end
  end

  # Returns a Hash of favorites for this user like weapons and character.
  # If the favorites haven't been parsed already, parsing is done now.
  def favorites
    return unless public?

    if @favorites.nil?
      @favorites = {}
      @favorites['campaign']                 = @xml_data.elements['stats/favorites/campaign'].text
      @favorites['campaign_percentage']      = @xml_data.elements['stats/favorites/campaignpct'].text.to_i
      @favorites['character']                = @xml_data.elements['stats/favorites/character'].text
      @favorites['character_percentage']     = @xml_data.elements['stats/favorites/characterpct'].text.to_i
      @favorites['level1_weapon']            = @xml_data.elements['stats/favorites/weapon1'].text
      @favorites['level1_weapon_percentage'] = @xml_data.elements['stats/favorites/weapon1pct'].text.to_i
      @favorites['level2_weapon']            = @xml_data.elements['stats/favorites/weapon2'].text
      @favorites['level2_weapon_percentage'] = @xml_data.elements['stats/favorites/weapon2pct'].text.to_i
    end

    @favorites
  end

  # Returns a Hash of lifetime statistics for this user like the time played.
  # If the lifetime statistics haven't been parsed already, parsing is done now.
  def lifetime_stats
    return unless public?

    if @lifetime_stats.nil?
      @lifetime_stats = {}
      @lifetime_stats['finales_survived'] = @xml_data.elements['stats/lifetime/finales'].text.to_i
      @lifetime_stats['games_played']     = @xml_data.elements['stats/lifetime/gamesplayed'].text.to_i
      @lifetime_stats['infected_killed']  = @xml_data.elements['stats/lifetime/infectedkilled'].text.to_i
      @lifetime_stats['kills_per_hour']   = @xml_data.elements['stats/lifetime/killsperhour'].text.to_f
      @lifetime_stats['avg_kits_shared']  = @xml_data.elements['stats/lifetime/kitsshared'].text.to_f
      @lifetime_stats['avg_kits_used']    = @xml_data.elements['stats/lifetime/kitsused'].text.to_f
      @lifetime_stats['avg_pills_shared'] = @xml_data.elements['stats/lifetime/pillsshared'].text.to_f
      @lifetime_stats['avg_pills_used']   = @xml_data.elements['stats/lifetime/pillsused'].text.to_f
      @lifetime_stats['time_played']      = @xml_data.elements['stats/lifetime/timeplayed'].text

      @lifetime_stats['finales_survived_percentage'] = @lifetime_stats['finales_survived'].to_f / @lifetime_stats['games_played']
    end

    @lifetime_stats
  end

  # Returns a Hash of Survival statistics for this user like revived teammates.
  # If the Survival statistics haven't been parsed already, parsing is done now.
  def survival_stats
    return unless public?

    if @survival_stats.nil?
      @survival_stats = {}
      @survival_stats['gold_medals']   = @xml_data.elements['stats/survival/goldmedals'].text.to_i
      @survival_stats['silver_medals'] = @xml_data.elements['stats/survival/silvermedals'].text.to_i
      @survival_stats['bronze_medals'] = @xml_data.elements['stats/survival/bronzemedals'].text.to_i
      @survival_stats['rounds_played'] = @xml_data.elements['stats/survival/roundsplayed'].text.to_i
      @survival_stats['best_time']     = @xml_data.elements['stats/survival/besttime'].text.to_f
    end

    @survival_stats
  end

  # Returns a Hash of teamplay statistics for this user like revived teammates.
  # If the teamplay statistics haven't been parsed already, parsing is done now.
  def teamplay_stats
    return unless public?

    if @teamplay_stats.nil?
      @teamplay_stats = {}
      @teamplay_stats['revived']                       = @xml_data.elements['stats/teamplay/revived'].text.to_i
      @teamplay_stats['most_revived_difficulty']       = @xml_data.elements['stats/teamplay/reviveddiff'].text
      @teamplay_stats['avg_revived']                   = @xml_data.elements['stats/teamplay/revivedavg'].text.to_f
      @teamplay_stats['avg_was_revived']               = @xml_data.elements['stats/teamplay/wasrevivedavg'].text.to_f
      @teamplay_stats['protected']                     = @xml_data.elements['stats/teamplay/protected'].text.to_i
      @teamplay_stats['most_protected_difficulty']     = @xml_data.elements['stats/teamplay/protecteddiff'].text
      @teamplay_stats['avg_protected']                 = @xml_data.elements['stats/teamplay/protectedavg'].text.to_f
      @teamplay_stats['avg_was_protected']             = @xml_data.elements['stats/teamplay/wasprotectedavg'].text.to_f
      @teamplay_stats['friendly_fire_damage']          = @xml_data.elements['stats/teamplay/ffdamage'].text.to_i
      @teamplay_stats['most_friendly_fire_difficulty'] = @xml_data.elements['stats/teamplay/ffdamagediff'].text
      @teamplay_stats['avg_friendly_fire_damage']      = @xml_data.elements['stats/teamplay/ffdamageavg'].text.to_f
    end

    @teamplay_stats
  end

  # Returns a Hash of Versus statistics for this user like percentage of rounds
  # won.
  # If the Versus statistics haven't been parsed already, parsing is done now.
  def versus_stats
    return unless public?

    if @versus_stats.nil?
      @versus_stats = {}
      @versus_stats['games_played']                = @xml_data.elements['stats/versus/gamesplayed'].text.to_i
      @versus_stats['games_completed']             = @xml_data.elements['stats/versus/gamescompleted'].text.to_i
      @versus_stats['finales_survived']            = @xml_data.elements['stats/versus/finales'].text.to_i
      @versus_stats['points']                      = @xml_data.elements['stats/versus/points'].text.to_i
      @versus_stats['most_points_infected']        = @xml_data.elements['stats/versus/pointsas'].text
      @versus_stats['games_won']                   = @xml_data.elements['stats/versus/gameswon'].text.to_i
      @versus_stats['games_lost']                  = @xml_data.elements['stats/versus/gameslost'].text.to_i
      @versus_stats['highest_survivor_score']      = @xml_data.elements['stats/versus/survivorscore'].text.to_i

      @versus_stats['finales_survived_percentage'] = @versus_stats['finales_survived'].to_f / @versus_stats['games_played']      

      self.class.const_get(:SPECIAL_INFECTED).each do |infected|
        @versus_stats[infected] = {}
        @versus_stats[infected]['special_attacks'] = @xml_data.elements["stats/versus/#{infected}special"].text.to_i
        @versus_stats[infected]['most_damage']     = @xml_data.elements["stats/versus/#{infected}dmg"].text.to_i
        @versus_stats[infected]['avg_lifespan']    = @xml_data.elements["stats/versus/#{infected}lifespan"].text.to_i
      end
    end

    @versus_stats
  end

end
