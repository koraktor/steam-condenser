# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008, Sebastian Staudt
#
# $Id$

require "open-uri"
require "rexml/document"

require "steam/community/game_achievement"

# The GameStats class represents the game statistics for a single user and a
# specific game
class GameStats
  
  # Creates a GameStats object and fetchs data from Steam Community for the
  # given user and game
  def initialize(steam_id, game_name)
    @steam_id = steam_id
    
    @xml_data = REXML::Document.new(open("http://www.steamcommunity.com/id/#{@steam_id}/stats/#{game_name}?xml=1", {:proxy => true}).read).elements["playerstats"]
    
    @privacy_state = @xml_data.elements["privacyState"].text
    if @privacy_state == "public"
      @accumulated_points = @xml_data.elements["stats"].elements["accumulatedPoints"].text.to_i
      @app_id             = @xml_data.elements["game"].elements["gameLink"].text.match("http://store.steampowered.com/app/([0-9]+)")[1]
      @class_data         = @xml_data.elements["class"]
      @game_friendly_name = @xml_data.elements["game"].elements["gameFriendlyName"].text
      @game_name          = @xml_data.elements["game"].elements["gameName"].text
      @hours_played       = @xml_data.elements["stats"].elements["hoursPlayed"].text.to_f
    end
  end
  
  # Returns the achievements for this stats' user and game. If the achievements
  # haven't been parsed already, parsing is done now.
  def get_achievements
    if @achievements.nil?
      @achievements = Array.new
      @xml_data.elements["achievements"].elements.each("achievement") do |achievement|
        @achievements << GameAchievement.new(@steam_id, @app_id, achievement.elements["name"].text, (achievement.attributes["closed"].to_i == 1))
      end
      @achievement_data = nil
    end
    
    return @achievements
  end
  
end