# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'open-uri'
require 'rexml/document'

require 'steam/community/game_achievement'
require 'steam/community/game_leaderboard'

# This class represents the game statistics for a single user and a specific
# game
#
# It is subclassed for individual games if the games provide special statistics
# that are unique to this game.
#
# @author Sebastian Staudt
class GameStats

  # Returns the Steam application ID of the game these stats belong to
  #
  # @return [Fixnum] The Steam application ID of the game
  attr_reader :app_id

  # Returns the custom URL of the player these stats belong to
  #
  # @return [String] The custom URL of the player
  attr_reader :custom_url

  # Returns the friendly name of the game these stats belong to
  #
  # @return [String ]The frienldy name of the game
  attr_reader :game_friendly_name

  # Returns the full name of the game these stats belong to
  #
  # @return [String] The name of the game
  attr_reader :game_name

  # Returns the number of hours this game has been played by the player
  #
  # @return [String] The number of hours this game has been played
  attr_reader :hours_played

  # Returns the privacy setting of the Steam ID profile
  #
  # @return [String] The privacy setting of the Steam ID
  attr_reader :privacy_state

  # Returns the 64bit numeric SteamID of the player these stats belong to
  #
  # @return [Fixnum] The 64bit numeric SteamID of the player
  attr_reader :steam_id64

  # Creates a `GameStats` (or one of its subclasses) instance for the given
  # user and game
  #
  # @param [String, Fixnum] steam_id The custom URL or the 64bit Steam ID of
  #        the user
  # @param [String] game_name The friendly name of the game
  # @return [GameStats] The game stats object for the given user and game
  def self.create_game_stats(steam_id, game_name)
    case game_name
      when 'alienswarm'
        require 'steam/community/alien_swarm/alien_swarm_stats'
        AlienSwarmStats.new(steam_id)
      when 'cs:s'
        require 'steam/community/css/css_stats'
        CSSStats.new(steam_id)
      when 'defensegrid:awakening'
        require 'steam/community/defense_grid/defense_grid_stats'
        DefenseGridStats.new(steam_id)
      when 'dod:s'
        require 'steam/community/dods/dods_stats'
        DoDSStats.new(steam_id)
      when 'l4d'
        require 'steam/community/l4d/l4d_stats'
        L4DStats.new(steam_id)
      when 'l4d2'
        require 'steam/community/l4d/l4d2_stats'
        L4D2Stats.new(steam_id)
      when 'portal2'
        require 'steam/community/portal2/portal2_stats'
        Portal2Stats.new steam_id
      when 'tf2'
        require 'steam/community/tf2/tf2_stats'
        TF2Stats.new(steam_id)
      else
        new(steam_id, game_name)
    end
  end

  # Creates a `GameStats` object and fetches data from the Steam Community for
  # the given user and game
  #
  # @param [String, Fixnum] id The custom URL or the 64bit Steam ID of the
  #        user
  # @param [String] game_name The friendly name of the game
  # @raise [SteamCondenserError] if the stats cannot be fetched
  def initialize(id, game_name)
    if id.is_a? Numeric
      @steam_id64 = id
    else
      @custom_url = id.downcase
    end
    @game_friendly_name = game_name

    url = base_url + '?xml=all'
    @xml_data = REXML::Document.new(open(url, {:proxy => true}).read).root

    error = @xml_data.elements['error']
    raise SteamCondenserError, error.text unless error.nil?

    @privacy_state = @xml_data.elements['privacyState'].text
    if public?
      @app_id       = @xml_data.elements['game/gameLink'].text.match(/http:\/\/store.steampowered.com\/app\/([1-9][0-9]+)/)[1].to_i
      @custom_url   = @xml_data.elements['player/customURL'].text if @custom_url.nil?
      @game_name    = @xml_data.elements['game/gameName'].text
      @hours_played = @xml_data.elements['stats/hoursPlayed'].text unless @xml_data.elements['stats/hoursPlayed'].nil?
      @steam_id64   = @xml_data.elements['player/steamID64'].text.to_i if @steam_id64.nil?
    end
  end

  # Returns the achievements for this stats' user and game
  #
  # If the achievements' data hasn't been parsed yet, parsing is done now.
  #
  # @return [Array<GameAchievement>] All achievements belonging to this game
  def achievements
    return unless public?

    if @achievements.nil?
      @achievements = Array.new
      @xml_data.elements.each('achievements/achievement') do |achievement_data|
        @achievements << GameAchievement.new(@steam_id64, @app_id, achievement_data)
      end

      @achievements_done = @achievements.reject{ |a| !a.unlocked? }.size
    end

    @achievements
  end

  # Returns the number of achievements done by this player
  #
  # If achievements haven't been parsed yet for this player and this game,
  # parsing is done now.
  #
  # @return [Fixnum] The number of achievements completed
  # @see #achievements
  def achievements_done
    achievements if @achievements_done.nil?
    @achievements_done
  end

  # Returns the percentage of achievements done by this player
  #
  # If achievements haven't been parsed yet for this player and this game,
  # parsing is done now.
  #
  # @return [Float] The percentage of achievements completed
  # @see #achievements_done
  def achievements_percentage
    achievements_done.to_f / @achievements.size
  end

  # Returns the base Steam Communtiy URL for the stats contained in this object
  #
  # @return [String] The base URL used for queries on these stats
  def base_url
    if @custom_url.nil?
      "http://steamcommunity.com/profiles/#{@steam_id64}/stats/#{@game_friendly_name}"
    else
      "http://steamcommunity.com/id/#{@custom_url}/stats/#{@game_friendly_name}"
    end
  end

  # Returns the leaderboard for this game and the given leaderboard ID or name
  #
  # @param [Fixnum, String] id The ID or name of the leaderboard to return
  # @return [GameLeaderboard] The matching leaderboard if available
  def leaderboard(id)
    GameLeaderboard.leaderboard @game_friendly_name, id
  end

  # Returns an array containing all of this game's leaderboards
  #
  # @return [Array<GameLeaderboard>] The leaderboards for this game
  def leaderboards
    GameLeaderboard.leaderboards @game_friendly_name
  end

  # Returns whether this Steam ID is publicly accessible
  #
  # @return [Boolean] `true` if this Steam ID is publicly accessible
  def public?
    @privacy_state == 'public'
  end

end
