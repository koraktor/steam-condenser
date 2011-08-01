# This code is free software; you can redistribute it and/or modify it under
# the terms of the new BSD License.
#
# Copyright (c) 2008-2011, Sebastian Staudt

require 'multi_json'

require 'steam/community/web_api'

# The GameAchievement class represents a specific achievement for a single game
# and for a single user
#
# It also provides the ability to load the global unlock percentages of all
# achievements of a specific game.
#
# @author Sebastian Staudt
class GameAchievement

  # Return the unique Steam Application ID of the  game this achievement
  # belongs to
  #
  # @return [Fixnum] The Steam Application ID of this achievement's game
  attr_reader :app_id

  # Returns the name of this achievement
  #
  # @return [String] The name of this achievement
  attr_reader :name

  # Returns the 64bit SteamID of the user who owns this achievement
  #
  # @return [Fixnum] The 64bit SteamID of this achievement's owner
  attr_reader :steam_id64

  # Returns the time this achievement has been unlocked by its owner
  #
  # @return [Time] The time this achievement has been unlocked
  attr_reader :timestamp

  # Loads the global unlock percentages of all achievements for the game with
  # the given Steam Application ID
  #
  # @param [Fixnum] app_id The unique Steam Application ID of the game (e.g.
  #        `440` for Team Fortress 2). See
  #         http://developer.valvesoftware.com/wiki/Steam_Application_IDs for
  #         all application IDs
  # @raise [WebApiError] if the request to Steam's Web API fails
  # @return [Hash<Symbol, Float>] The symbolic achievement names with their
  #         corresponding unlock percentages
  def self.global_percentages(app_id)
    percentages = {}

    data = WebApi.json('ISteamUserStats', 'GetGlobalAchievementPercentagesForApp', 2, { :gameid => app_id })
    MultiJson.decode(data, { :symbolize_keys => true })[:achievementpercentages][:achievements].each do |percentage|
      percentages[percentage[:name].to_sym] = percentage[:percent]
    end

    percentages
  end

  # Creates the achievement with the given name for the given user and game
  # and achievement data
  #
  # @param [Fixnum] steam_id64 The 64bit SteamID of the player this achievement
  #        belongs to
  # @param [Fixnum] app_id The unique Steam Application ID of the game (e.g.
  #        `440` for Team Fortress 2). See
  #        http://developer.valvesoftware.com/wiki/Steam_Application_IDs for
  #        all application IDs
  # @param [REXML::Element] achievement_data The achievement data extracted
  #        from XML
  def initialize(steam_id64, app_id, achievement_data)
    @app_id     = app_id
    @name       = achievement_data.elements['name'].text
    @steam_id64 = steam_id64
    @unlocked   = (achievement_data.attributes['closed'].to_i == 1)

    if @unlocked && !achievement_data.elements['unlockTimestamp'].nil?
      @timestamp  = Time.at(achievement_data.elements['unlockTimestamp'].text.to_i)
    end
  end

  # Returns whether this achievement has been unlocked by its owner
  #
  # @return [Boolean] `true` if the achievement has been unlocked by the user
  def unlocked?
    @unlocked
  end

end
