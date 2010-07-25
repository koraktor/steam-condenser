# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'json'

require 'steam/community/web_api'

# The GameAchievement class represents a specific achievement for a single game
# and for a single user
class GameAchievement

  attr_reader :app_id, :name, :steam_id64, :timestamp

  # Loads the global unlock percentages of all achievements for the given game
  #
  # [+app_id+] The unique Steam Application ID of the game (e.g. +440+ for
  #            Team Fortress 2). See
  #            http://developer.valvesoftware.com/wiki/Steam_Application_IDs
  #            for all application IDs
  def self.global_percentages(app_id)
    percentages = {}

    data = WebApi.json('ISteamUserStats', 'GetGlobalAchievementPercentagesForApp', 1, { :gameid => app_id })
    JSON.parse(data, { :symbolize_names => true })[:achievementpercentages][:achievements][:achievement].each do |percentage|
      percentages[percentage[:name].to_sym] = percentage[:percent]
    end

    percentages
  end

  # Creates the achievement with the given name for the given user and game
  # and achievement data
  #
  # [+steam_id64+]       The 64bit SteamID of the player this achievement
  #                      belongs to
  # [+app_id+]           The unique Steam Application ID of the game (e.g.
  #                      +440+ for Team Fortress 2). See
  #                      http://developer.valvesoftware.com/wiki/Steam_Application_IDs
  #                      for all application IDs
  # [+achievement_data+] The achievement data extracted from JSON
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
  def unlocked?
    @unlocked
  end

end
