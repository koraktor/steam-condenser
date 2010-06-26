# This code is free software; you can redistribute it and/or modify it under the
# terms of the new BSD License.
#
# Copyright (c) 2008-2010, Sebastian Staudt

require 'open-uri'
require 'rexml/document'

require 'steam/community/game_achievement'

class GameStats
end

require 'steam/community/css/css_stats'
require 'steam/community/defense_grid/defense_grid_stats'
require 'steam/community/dods/dods_stats'
require 'steam/community/l4d/l4d_stats'
require 'steam/community/l4d/l4d2_stats'
require 'steam/community/tf2/tf2_stats'

# The GameStats class represents the game statistics for a single user and a
# specific game
class GameStats

  protected :initialize

  attr_reader :app_id, :custom_url, :game_friendly_name, :game_name,
              :hours_played, :privacy_state, :steam_id64

  # Creates a GameStats (or one of its subclasses) object for the given user
  # depending on the game selected
  def self.create_game_stats(steam_id, game_name)
    case game_name
      when 'cs:s'
        CSSStats.new(steam_id)
      when 'defensegrid:awakening'
        DefenseGridStats.new(steam_id)
      when 'dod:s'
        DoDSStats.new(steam_id)
      when 'l4d'
        L4DStats.new(steam_id)
      when 'l4d2'
        L4D2Stats.new(steam_id)
      when 'tf2'
        TF2Stats.new(steam_id)
      else
        new(steam_id, game_name)
    end
  end

  # Creates a GameStats object and fetches data from the Steam Community for
  # the given user and game
  def initialize(id, game_name)
    if id.is_a? Numeric
      @steam_id64 = id
    else
      @custom_url = id.downcase
    end
    @game_friendly_name = game_name

    url = base_url + '?xml=1'
    @xml_data = REXML::Document.new(open(url, {:proxy => true}).read).root

    @privacy_state = @xml_data.elements['privacyState'].text
    if public?
      @app_id             = @xml_data.elements['game/gameLink'].text.match(/http:\/\/store.steampowered.com\/app\/([1-9][0-9]+)/)[1]
      @custom_url         = @xml_data.elements['player/customURL'].text if @custom_url.nil?
      @game_name          = @xml_data.elements['game/gameName'].text
      @hours_played       = @xml_data.elements['stats/hoursPlayed'].text
      @steam_id64         = @xml_data.elements['player/steamID64'].text.to_i if @steam_id64.nil?
    end
  end

  # Returns the achievements for this stats' user and game. If the achievements
  # haven't been parsed already, parsing is done now.
  def achievements
    return unless public?

    if @achievements.nil?
      @achievements = Array.new
      @xml_data.elements.each('achievements/achievement') do |achievement|
        @achievements << GameAchievement.new(@steam_id, @app_id, achievement.elements['name'].text, (achievement.attributes['closed'].to_i == 1))
      end

      @achievements_done = @achievements.reject{ |a| !a.done? }.size
    end

    @achievements
  end

  # Returns the count of achievements done by this player. If achievements
  # haven't been parsed yet, parsing is done now.
  def achievements_done
    achievements if @achievements_done.nil?
    @achievements_done
  end

  # Returns a float value representing the percentage of achievements done by
  # this player.
  def achievements_percentage
    achievements_done.to_f / @achievements.size
  end

  # Returns the base URL for this Steam Communtiy object
  def base_url
    if @custom_url.nil?
      "http://steamcommunity.com/profiles/#{@steam_id64}/stats/#{@game_friendly_name}"
    else
      "http://steamcommunity.com/id/#{@custom_url}/stats/#{@game_friendly_name}"
    end
  end

  def public?
    @privacy_state == 'public'
  end

end
