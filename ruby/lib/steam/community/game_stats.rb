# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2009, Sebastian Staudt

require "open-uri"
require "rexml/document"

require "steam/community/game_achievement"

class GameStats
end

require "steam/community/tf2/tf2_stats"

# The GameStats class represents the game statistics for a single user and a
# specific game
class GameStats
  
  protected :initialize

  attr_reader :app_id, :game_friendly_name, :game_name, :hours_played,
              :privacy_state, :steam_id

  # Creates a GameStats (or one of its subclasses) object for the given user
  # depending on the game selected
  def self.create_game_stats(steam_id, game_name)
    case game_name
      when "TF2":
        return TF2Stats.new(steam_id)
      else
        return self.new(steam_id, game_name)
    end
  end
  
  # Creates a GameStats object and fetchs data from Steam Community for the
  # given user and game
  def initialize(steam_id, game_name)
    @steam_id = steam_id
    
    @xml_data = REXML::Document.new(open("http://www.steamcommunity.com/id/#{@steam_id}/stats/#{game_name}?xml=1", {:proxy => true}).read).elements["playerstats"]
    
    @privacy_state = @xml_data.elements["privacyState"].text
    if @privacy_state == "public"
      @app_id             = @xml_data.elements["game"].elements["gameLink"].text.match("http://store.steampowered.com/app/([0-9]+)")[1]
      @game_friendly_name = @xml_data.elements["game"].elements["gameFriendlyName"].text
      @game_name          = @xml_data.elements["game"].elements["gameName"].text
      @hours_played       = @xml_data.elements["stats"].elements["hoursPlayed"].text.to_f
    end
  end
  
  # Returns the achievements for this stats' user and game. If the achievements
  # haven't been parsed already, parsing is done now.
  def achievements
    if @achievements.nil?
      @achievements = Array.new
      @xml_data.elements["achievements"].elements.each("achievement") do |achievement|
        @achievements << GameAchievement.new(@steam_id, @app_id, achievement.elements["name"].text, (achievement.attributes["closed"].to_i == 1))
      end

      @achievements_done = 0
      @achievements.each do |achievement|
        @achievements_done += 1 if achievement.done?
      end
    end
    
    return @achievements
  end
  
end